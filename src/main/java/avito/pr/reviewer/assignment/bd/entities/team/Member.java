package avito.pr.reviewer.assignment.bd.entities.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String userId;
    private String username;
    private Boolean isActive;
}
