package dat.controllers.impl;

import dat.daos.impl.PerformanceDAO;
import dat.dtos.PerformanceDTO;

import java.util.List;

public class PerformanceController {
    private final PerformanceDAO dao;

    public PerformanceController(PerformanceDAO dao) {
        this.dao = dao;
    }

    public List<PerformanceDTO> getAll() {
        return dao.getAll();
    }

    public PerformanceDTO getById(int id) {
        return dao.getById(id);
    }

    public PerformanceDTO create(PerformanceDTO dto) {
        return dao.create(dto);
    }

    public PerformanceDTO update(int id, PerformanceDTO dto) {
        return dao.update(id, dto);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    public void addActorToPerformance(int performanceId, int actorId) {
        dao.addActorToPerformance(performanceId, actorId);
    }

    public void populateTestData() {
        dat.utils.Populator.populateTheaterData(dat.config.HibernateConfig.getEntityManagerFactory());
    }
}
