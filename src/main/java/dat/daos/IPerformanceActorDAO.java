package dat.daos;

import dat.dtos.PerformanceDTO;
import java.util.Set;

public interface IPerformanceActorDAO {
    void addActorToPerformance(int performanceId, int actorId);
    Set<PerformanceDTO> getPerformancesByActor(int actorId);
}
