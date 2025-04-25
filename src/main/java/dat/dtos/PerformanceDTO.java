package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Genre;
import dat.entities.Performance;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceDTO {
    private int id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double ticketPrice;
    private double latitude;
    private double longitude;
    private Genre genre;
    private ActorDTO actor;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public PerformanceDTO(Performance performance) {
        this.id = performance.getId();
        this.title = performance.getTitle();
        this.startTime = performance.getStartTime();
        this.endTime = performance.getEndTime();
        this.ticketPrice = performance.getTicketPrice();
        this.latitude = performance.getLatitude();
        this.longitude = performance.getLongitude();
        this.genre = performance.getGenre();
        if (performance.getActor() != null) {
            this.actor = new ActorDTO(performance.getActor());
        }
    }



}
