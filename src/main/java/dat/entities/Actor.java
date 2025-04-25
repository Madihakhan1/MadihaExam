package dat.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int yearsOfExperience;

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL)
    private Set<Performance> performances = new HashSet<>();

    public void addPerformance(Performance performance) {
        performances.add(performance);
        performance.setActor(this);
    }
}
