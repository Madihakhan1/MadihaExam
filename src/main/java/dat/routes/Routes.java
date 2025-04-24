package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    /*private final HotelRoute hotelRoute = new HotelRoute();
    private final RoomRoute roomRoute = new RoomRoute();
*/
    private final PopulatorRoutes populator = new PopulatorRoutes();
    public EndpointGroup getRoutes() {
        return () -> {
            path("/populator",populator.getRoutes()); {

            };
                /*path("/hotels", hotelRoute.getRoutes());
                path("/rooms", roomRoute.getRoutes());

                 */
        };
    }
}
