package dat;

import dat.config.ApplicationConfig;
import dat.routes.Routes;

public class Main {

    public static void main(String[] args) {


        ApplicationConfig.startServer(7007);

        new Routes().getRoutes();
    }


    }


