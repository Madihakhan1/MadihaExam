package dat.routes;

import dat.utils.Populator;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;


public class PopulatorRoutes {

    private final EntityManagerFactory emf;

    public PopulatorRoutes(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/users", ctx -> {
                Populator.populateUsers(emf);
                ctx.result(" Users populated");
            });

            get("/theater", ctx -> {
                Populator.populateTheaterData(emf);
                ctx.result(" Theater data populated");
            });
        };
    }
}

