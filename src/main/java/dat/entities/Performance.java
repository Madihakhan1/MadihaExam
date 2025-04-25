package dat.entities;

import dat.dtos.PerformanceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.JdkConstants;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double ticketPrice;

    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToOne
    private Actor actor;

    public Performance(PerformanceDTO dto) {
        this.title = dto.getTitle();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.ticketPrice = dto.getTicketPrice();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.genre = dto.getGenre();
    }

    public void updateFromDTO(PerformanceDTO dto) {
        this.title = dto.getTitle();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.ticketPrice = dto.getTicketPrice();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.genre = dto.getGenre();
    }



}
