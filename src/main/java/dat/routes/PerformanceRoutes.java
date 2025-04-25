package dat.routes;

import dat.controllers.impl.PerformanceController;
import dat.dtos.PerformanceDTO;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PerformanceRoutes {

    private final PerformanceController controller;

    public PerformanceRoutes(PerformanceController controller) {
        this.controller = controller;
    }

    public EndpointGroup getRoutes() {
        return () -> {

            // Get all performances (anyone can access)
            get("", ctx -> ctx.json(controller.getAll()), Role.ANYONE);

            // Get performance by ID (anyone can access)
            get("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                PerformanceDTO dto = controller.getById(id);
                if (dto != null) {
                    ctx.status(200).json(dto);
                } else {
                    ctx.status(404).json(Map.of("error", "Performance not found"));
                }
            }, Role.ANYONE);

            // Create performance (admin only)
            post("", ctx -> {
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                PerformanceDTO created = controller.create(dto);
                ctx.status(201).json(created);
            }, Role.ADMIN);

            // Update performance (admin only)
            put("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                PerformanceDTO dto = ctx.bodyAsClass(PerformanceDTO.class);
                PerformanceDTO updated = controller.update(id, dto);
                if (updated != null) {
                    ctx.status(200).json(updated);
                } else {
                    ctx.status(404).json(Map.of("error", "Performance not found"));
                }
            }, Role.ADMIN);

            // Delete performance (admin only)
            delete("/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                boolean deleted = controller.delete(id);
                if (deleted) {
                    ctx.status(204);
                } else {
                    ctx.status(404).json(Map.of("error", "Performance not found"));
                }
            }, Role.ADMIN);

            // Add actor to performance (anyone can access)
            put("/{performanceId}/actors/{actorId}", ctx -> {
                int performanceId = Integer.parseInt(ctx.pathParam("performanceId"));
                int actorId = Integer.parseInt(ctx.pathParam("actorId"));
                controller.addActorToPerformance(performanceId, actorId);
                ctx.status(204);
            }, Role.ANYONE);

            // Populate test data (admin only)
            post("/populate", ctx -> {
                controller.populateTestData();
                ctx.status(201).result("Database populated");
            }, Role.ADMIN);
        };
    }
}
