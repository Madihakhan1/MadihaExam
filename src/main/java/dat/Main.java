package dat;

import dat.config.ApplicationConfig;
import dat.routes.Routes;
import io.javalin.Javalin;


import dat.config.ApplicationConfig;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = ApplicationConfig.startServer(7007);
    }
}




