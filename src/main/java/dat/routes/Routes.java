package dat.routes;

import dat.controllers.impl.PerformanceController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

import dat.daos.impl.PerformanceDAO;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;

public class Routes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final PopulatorRoutes populatorRoutes = new PopulatorRoutes(emf);
    private final PerformanceRoutes performanceRoutes = new PerformanceRoutes(new PerformanceController(new PerformanceDAO(emf)));

    public EndpointGroup getRoutes() {
        return () -> {
            path("/populator", populatorRoutes.getRoutes());
            path("/performances", performanceRoutes.getRoutes());
        };
    }
}

