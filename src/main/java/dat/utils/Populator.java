package dat.utils;

import dat.security.entities.User;
import dat.security.entities.Role;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManagerFactory;

import dat.entities.Actor;
import dat.entities.Genre;
import dat.entities.Performance;


import java.time.LocalDateTime;

public class Populator {
    public static void clearDatabase(EntityManagerFactory emf) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Ryd først relationstabeller for at undgå foreign key problemer
            em.createNativeQuery("DELETE FROM user_roles").executeUpdate();

            // Ryd hovedtabeller
            em.createQuery("DELETE FROM Performance").executeUpdate();
            em.createQuery("DELETE FROM Actor").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();

            em.getTransaction().commit();
            System.out.println("Database cleared!");
        } catch (Exception e) {
            System.err.println("Failed to clear database: " + e.getMessage());
        }
    }


    public static UserDTO[] populateUsers(EntityManagerFactory emf) {
        User user, admin;
        Role userRole, adminRole;

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Tjek om roller allerede findes
            userRole = em.find(Role.class, "USER");
            if (userRole == null) {
                userRole = new Role("USER");
                em.persist(userRole);
            }

            adminRole = em.find(Role.class, "ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ADMIN");
                em.persist(adminRole);
            }

            user = new User("usertest", "user123");
            admin = new User("admintest", "admin123");

            user.addRole(userRole);
            admin.addRole(adminRole);

            em.persist(user);
            em.persist(admin);

            em.getTransaction().commit();
        }

        UserDTO userDTO = new UserDTO("usertest", "user123");
        UserDTO adminDTO = new UserDTO("admintest", "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }


    public static void populateTheaterData(EntityManagerFactory emf) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();


            Actor actor1 = Actor.builder()
                    .firstName("Emma")
                    .lastName("Thompson")
                    .email("emma@example.com")
                    .phone("12345678")
                    .yearsOfExperience(12)
                    .build();

            Actor actor2 = Actor.builder()
                    .firstName("John")
                    .lastName("Smith")
                    .email("john@example.com")
                    .phone("87654321")
                    .yearsOfExperience(8)
                    .build();

            Performance p1 = Performance.builder()
                    .title("Hamlet")
                    .startTime(LocalDateTime.now().plusDays(1))
                    .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                    .ticketPrice(150)
                    .latitude(55.6761)
                    .longitude(12.5683)
                    .genre(Genre.DRAMA)
                    .actor(actor1)
                    .build();

            Performance p2 = Performance.builder()
                    .title("Comedy Night")
                    .startTime(LocalDateTime.now().plusDays(3))
                    .endTime(LocalDateTime.now().plusDays(3).plusHours(1))
                    .ticketPrice(100)
                    .latitude(55.6761)
                    .longitude(12.5683)
                    .genre(Genre.COMEDY)
                    .actor(actor2)
                    .build();

            em.persist(actor1);
            em.persist(actor2);
            em.persist(p1);
            em.persist(p2);

            em.getTransaction().commit();
            System.out.println("Theater data populated!");
        } catch (Exception e) {
            System.out.println("Failed to populate theater data: " + e.getMessage());
        }
    }
}
