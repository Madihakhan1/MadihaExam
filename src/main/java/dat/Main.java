package dat;

import dat.config.ApplicationConfig;
import dat.controllers.impl.ExceptionController;
import dat.controllers.impl.PerformanceController;
import dat.daos.impl.PerformanceDAO;
import dat.routes.PerformanceRoutes;
import dat.config.HibernateConfig;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Start serveren
        Javalin app = ApplicationConfig.startServer(7007);

        // Opret controller og routes
        PerformanceController performanceController = new PerformanceController(
                new PerformanceDAO(HibernateConfig.getEntityManagerFactory())
        );
        PerformanceRoutes performanceRoutes = new PerformanceRoutes(performanceController);

        // Exception handling
        ExceptionController exceptionController = new ExceptionController();
        app.exception(dat.exceptions.ApiException.class, exceptionController::apiExceptionHandler);
        app.exception(Exception.class, exceptionController::exceptionHandler);

    }
}
