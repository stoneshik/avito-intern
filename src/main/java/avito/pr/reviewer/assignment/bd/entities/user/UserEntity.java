package avito.pr.reviewer.assignment.bd.entities.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private String userId;
    private String username;
    private String teamName;
    private Boolean isActive;
}
