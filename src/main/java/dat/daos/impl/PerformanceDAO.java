package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.IPerformanceActorDAO;
import dat.dtos.PerformanceDTO;
import dat.entities.Actor;
import dat.entities.Genre;
import dat.entities.Performance;
import dat.exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PerformanceDAO implements IDAO<PerformanceDTO, Integer>, IPerformanceActorDAO {
    private final EntityManagerFactory emf;

    public PerformanceDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public PerformanceDTO create(PerformanceDTO performanceDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Performance performance = new Performance(performanceDTO);
            em.persist(performance);
            em.getTransaction().commit();
            return new PerformanceDTO(performance);
        } catch (Exception e) {
            throw new DAOException("Error creating performance", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PerformanceDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Performance> performances = em.createQuery("SELECT p FROM Performance p", Performance.class)
                    .getResultList();
            return performances.stream().map(PerformanceDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DAOException("Error fetching performances", e);
        } finally {
            em.close();
        }
    }

    @Override
    public PerformanceDTO getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Performance performance = em.find(Performance.class, id);
            if (performance == null) {
                throw new DAOException("Performance with id " + id + " not found");
            }
            return new PerformanceDTO(performance);
        } catch (Exception e) {
            throw new DAOException("Error retrieving performance", e);
        } finally {
            em.close();
        }
    }

    @Override
    public PerformanceDTO update(int id, PerformanceDTO performanceDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Performance performance = em.find(Performance.class, id);
            if (performance == null) {
                em.getTransaction().rollback();
                throw new DAOException("Performance with id " + id + " not found");
            }
            performance.updateFromDTO(performanceDTO);
            em.merge(performance);
            em.getTransaction().commit();
            return new PerformanceDTO(performance);
        } catch (Exception e) {
            throw new DAOException("Error updating performance", e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Performance performance = em.find(Performance.class, id);
            if (performance == null) {
                em.getTransaction().rollback();
                throw new DAOException("Performance with id " + id + " not found");
            }
            em.remove(performance);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new DAOException("Error deleting performance", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void addActorToPerformance(int performanceId, int actorId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Performance performance = em.find(Performance.class, performanceId);
            Actor actor = em.find(Actor.class, actorId);
            if (performance == null || actor == null) {
                em.getTransaction().rollback();
                throw new DAOException("Performance or Actor not found");
            }
            performance.setActor(actor);
            em.merge(performance);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new DAOException("Error adding actor to performance", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Set<PerformanceDTO> getPerformancesByActor(int actorId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Performance> performances = em.createQuery(
                            "SELECT p FROM Performance p WHERE p.actor.id = :actorId", Performance.class)
                    .setParameter("actorId", actorId)
                    .getResultList();
            return performances.stream()
                    .map(PerformanceDTO::new)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new DAOException("Error fetching performances by actor", e);
        } finally {
            em.close();
        }
    }

    public List<PerformanceDTO> getPerformancesByGenre(Genre genre) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Performance> performances = em.createQuery(
                            "SELECT p FROM Performance p WHERE p.genre = :genre", Performance.class)
                    .setParameter("genre", genre)
                    .getResultList();
            return performances.stream().map(PerformanceDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DAOException("Error fetching performances by genre", e);
        } finally {
            em.close();
        }
    }

    public List<Map<String, Object>> getActorOverviewByRevenue() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                            "SELECT a.id, SUM(p.ticketPrice) FROM Performance p JOIN p.actor a GROUP BY a.id", Object[].class)
                    .getResultList();
            return results.stream()
                    .map(result -> {
                        Map<String, Object> actorRevenue = new HashMap<>();
                        actorRevenue.put("actorId", result[0]);
                        actorRevenue.put("totalRevenue", result[1]);
                        return actorRevenue;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DAOException("Error fetching actor overview by revenue", e);
        } finally {
            em.close();
        }
    }
}
