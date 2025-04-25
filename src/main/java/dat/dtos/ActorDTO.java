package dat.dtos;

import dat.entities.Actor;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActorDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int yearsOfExperience;

    public ActorDTO(Actor actor) {
        this.id = actor.getId();
        this.firstName = actor.getFirstName();
        this.lastName = actor.getLastName();
        this.email = actor.getEmail();
        this.phone = actor.getPhone();
        this.yearsOfExperience = actor.getYearsOfExperience();
    }
}
