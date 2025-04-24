package dat.routes;

import dat.config.HibernateConfig;
import dat.security.enums.Role;
import dat.utils.Populator;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PopulatorRoutes {


    protected EndpointGroup getRoutes() {


        //OBS denne ville ikke fungere med test, da den er bundet til den "rigtige" databse.
        // Alternativ skal man lave en ny constructor og skyde emf ind udefra

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        return () -> {
            get("/", ctx -> {
                Populator.populateUsers(emf);
                ctx.result("Populates");
            });
        };

    }
}
