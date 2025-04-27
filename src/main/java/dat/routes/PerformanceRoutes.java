package dat.routes;

import dat.controllers.impl.PerformanceController;
import dat.dtos.PerformanceDTO;
import dat.entities.Genre;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PerformanceRoutes {

    private final PerformanceController controller;

    public PerformanceRoutes(PerformanceController controller) {
        this.controller = controller;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/performances", ctx -> ctx.json(controller.getAll()));
            get("/performances/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                PerformanceDTO dto = controller.getById(id);
                if (dto != null) {
                    ctx.status(200).json(dto);
                } else {
                    ctx.status(404).json(Map.of("error", "Performance not found"));
                }
            });

            post("/performances", ctx -> {
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                PerformanceDTO created = controller.create(dto);
                ctx.status(201).json(created);
            });

            put("/performances/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                PerformanceDTO updated = controller.update(id, dto);
                if (updated != null) {
                    ctx.status(200).json(updated);
                } else {
                    ctx.status(404).json(Map.of("error", "Performance not found"));
                }
            });

            delete("/performances/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                boolean deleted = controller.delete(id);
                if (deleted) {
                    ctx.status(204);
                } else {
                    ctx.status(404).json(Map.of("error", "Performance not found"));
                }
            });

            get("/performances/actors/overview", ctx -> {
                List<Map<String, Object>> overview = controller.getActorOverviewByRevenue();
                ctx.json(overview);
            });

        };
    }
}
