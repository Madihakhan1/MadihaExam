package dat.routes;

import dat.controllers.impl.PerformanceController;
import dat.dtos.PerformanceDTO;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.List;
import java.util.Map;

public class PerformanceRoutes {

    private final PerformanceController controller;

    public PerformanceRoutes(PerformanceController controller) {
        this.controller = controller;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            // Bemærk: ingen ekstra "/performances" her!

            get("/", ctx -> ctx.json(controller.getAll()));

            get("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                ctx.json(controller.getById(id));
            });

            post("/", ctx -> {
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                ctx.status(201).json(controller.create(dto));
            });

            put("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                ctx.json(controller.update(id, dto));
            });

            delete("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                controller.delete(id);
                ctx.status(204);
            });

            get("/actors/overview", ctx -> {
                List<Map<String, Object>> overview = controller.getActorOverviewByRevenue();
                ctx.json(overview);
            });
        };
    }
}
