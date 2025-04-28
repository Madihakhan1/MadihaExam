package dat.controllers.impl;

import dat.daos.impl.PerformanceDAO;
import dat.dtos.PerformanceDTO;
import dat.entities.Genre;
import dat.exceptions.ApiException;
import dat.exceptions.DAOException;

import java.util.List;
import java.util.Map;

public class PerformanceController {
    private final PerformanceDAO dao;

    public PerformanceController(PerformanceDAO dao) {
        this.dao = dao;
    }

    public List<PerformanceDTO> getAll() {
        return dao.getAll();
    }

    public PerformanceDTO getById(int id) throws ApiException {
        try {
            return dao.getById(id);
        } catch (DAOException e) {
            if (e.getMessage().toLowerCase().contains("not found")) {
                throw new ApiException(404, e.getMessage());
            }
            throw new ApiException(500, "Internal server error");
        }
    }

    public PerformanceDTO create(PerformanceDTO dto) {
        return dao.create(dto);
    }

    public PerformanceDTO update(int id, PerformanceDTO dto) throws ApiException {
        try {
            return dao.update(id, dto);
        } catch (DAOException e) {
            if (e.getMessage().toLowerCase().contains("not found")) {
                throw new ApiException(404, e.getMessage());
            }
            throw new ApiException(500, "Internal server error");
        }
    }

    public void delete(int id) throws ApiException {
        try {
            boolean deleted = dao.delete(id);
            if (!deleted) {
                throw new ApiException(404, "Performance with id " + id + " not found");
            }
        } catch (DAOException e) {
            throw new ApiException(500, "Internal server error");
        }
    }

    public void addActorToPerformance(int performanceId, int actorId) throws ApiException {
        try {
            dao.addActorToPerformance(performanceId, actorId);
        } catch (DAOException e) {
            if (e.getMessage().toLowerCase().contains("not found")) {
                throw new ApiException(404, e.getMessage());
            }
            throw new ApiException(500, "Internal server error");
        }
    }

    public void populateTestData() {
        dat.utils.Populator.populateTheaterData(dat.config.HibernateConfig.getEntityManagerFactory());
    }

    public List<PerformanceDTO> getPerformancesByGenre(Genre genre) {
        return dao.getPerformancesByGenre(genre);
    }

    public List<Map<String, Object>> getActorOverviewByRevenue() {
        return dao.getActorOverviewByRevenue();
    }
}

