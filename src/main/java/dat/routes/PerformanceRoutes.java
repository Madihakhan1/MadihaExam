package dat.routes;


import dat.controllers.impl.PerformanceController;
import dat.dtos.PerformanceDTO;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PerformanceRoutes {

    private final PerformanceController controller;

    public PerformanceRoutes(PerformanceController controller) {
        this.controller = controller;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", ctx -> ctx.json(controller.getAll()));
            get("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                ctx.json(controller.getById(id));
            });
            post("/", ctx -> {
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                ctx.json(controller.create(dto));
            });
            put("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                ctx.json(controller.update(id, dto));
            });
            delete("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                boolean deleted = controller.delete(id);
                ctx.status(deleted ? 204 : 404);
            });
            put("/{performanceId}/actors/{actorId}", ctx -> {
                int performanceId = Integer.parseInt(ctx.pathParam("performanceId"));
                int actorId = Integer.parseInt(ctx.pathParam("actorId"));
                controller.addActorToPerformance(performanceId, actorId);
                ctx.status(204);
            });
            post("/populate", ctx -> {
                controller.populateTestData();
                ctx.status(201).result("Database populated");
            });
        };
    }
}

