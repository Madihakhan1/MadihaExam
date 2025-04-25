package dat.controllers.impl;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.PerformanceDAO;
import dat.dtos.PerformanceDTO;
import dat.entities.Genre;

import dat.security.entities.Role;
import dat.security.entities.User;
import dat.utils.Populator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PerformanceControllerTest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final PerformanceDAO performanceDAO = new PerformanceDAO(emf);
    private static final PerformanceController controller = new PerformanceController(performanceDAO);
    private static Javalin app;
    private static String token;
    private static final int PORT = 7007;

    @BeforeAll
    void setupAll() {
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(PORT);
        RestAssured.baseURI = "http://localhost:" + PORT + "/api";
    }

    @AfterAll
    void tearDown() {
        ApplicationConfig.stopServer(app);
    }

    @BeforeEach
    void setUpEach() {
        Populator.clearDatabase(emf);

        // Add test user with USER role
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Role userRole = em.find(Role.class, "USER");
            if (userRole == null) {
                userRole = new Role("USER");
                em.persist(userRole);
            }

            User user = new User("user", "test");
            user.addRole(userRole);
            em.persist(user);

            em.getTransaction().commit();
        }

        // Login and retrieve token
        String loginJson = """
        {
          "username": "user",
          "password": "test"
        }
        """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response();

        token = "Bearer " + response.jsonPath().getString("token");

        Populator.populateTheaterData(emf);
    }

    @Test
    void testGetAllPerformances() {
        List<PerformanceDTO> performances = given()
                .header("Authorization", token)
                .when()
                .get("/performances")
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath().getList("", PerformanceDTO.class);

        assertThat(performances, is(notNullValue()));
        assertThat(performances.size(), greaterThan(0));
    }

    @Test
    void testGetPerformanceById() {
        List<PerformanceDTO> performances = given()
                .header("Authorization", token)
                .when()
                .get("/performances")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", PerformanceDTO.class);

        assertFalse(performances.isEmpty());
        int id = performances.get(0).getId(); // <-- Brug rigtig ID

        PerformanceDTO dto = given()
                .header("Authorization", token)
                .when()
                .get("/performances/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(PerformanceDTO.class);

        assertThat(dto.getId(), equalTo(id));
        assertThat(dto.getTitle(), notNullValue());
    }


    @Test
    void testCreatePerformance() {
        PerformanceDTO newPerformance = PerformanceDTO.builder()
                .title("Hamlet")
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .ticketPrice(120)
                .latitude(55.6761)
                .longitude(12.5683)
                .genre(Genre.DRAMA)
                .build();

        PerformanceDTO created = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(newPerformance)
                .when()
                .post("/performances")
                .then()
                .statusCode(201)
                .extract().as(PerformanceDTO.class);

        assertThat(created.getId(), greaterThan(0));
        assertThat(created.getTitle(), equalTo("Hamlet"));
    }

    @Test
    void testUpdatePerformance() {
        // Først opret en performance
        PerformanceDTO newPerformance = PerformanceDTO.builder()
                .title("Test Performance")
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .ticketPrice(120)
                .latitude(55.6761)
                .longitude(12.5683)
                .genre(Genre.DRAMA)
                .build();

        int createdId = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(newPerformance)
                .when()
                .post("/performances")
                .then()
                .statusCode(201) // husk 201 her!
                .extract()
                .jsonPath().getInt("id");

        // Nu kan du opdatere den
        PerformanceDTO updatedPerformance = PerformanceDTO.builder()
                .title("Hamlet Updated")
                .startTime(LocalDateTime.now().plusDays(2))
                .endTime(LocalDateTime.now().plusDays(2).plusHours(3))
                .ticketPrice(140)
                .latitude(55.6761)
                .longitude(12.5683)
                .genre(Genre.COMEDY)
                .build();

        PerformanceDTO updated = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(updatedPerformance)
                .when()
                .put("/performances/" + createdId)
                .then()
                .statusCode(200)
                .extract()
                .as(PerformanceDTO.class);

        assertThat(updated.getTitle(), containsString("Updated"));
    }

    @Test
    void testDeletePerformance() {
        // 1. Opret en ny performance og få ID
        PerformanceDTO newPerformance = PerformanceDTO.builder()
                .title("Test Delete Performance")
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .ticketPrice(150)
                .latitude(55.6761)
                .longitude(12.5683)
                .genre(Genre.DRAMA)
                .build();

        int createdId = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(newPerformance)
                .when()
                .post("/performances")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath().getInt("id");

        // 2. Slet performance
        given()
                .header("Authorization", token)
                .when()
                .delete("/performances/" + createdId)
                .then()
                .statusCode(204);

        // 3. Bekræft at den er væk
        given()
                .header("Authorization", token)
                .when()
                .get("/performances/" + createdId)
                .then()
                .statusCode(404);
    }


    @Test
    void testAddActorToPerformance() {
        given()
                .header("Authorization", token)
                .when()
                .put("/performances/2/actors/1")
                .then()
                .statusCode(204);
    }
}