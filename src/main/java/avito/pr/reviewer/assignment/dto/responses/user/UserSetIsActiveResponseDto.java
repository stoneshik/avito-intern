package avito.pr.reviewer.assignment.dto.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSetIsActiveResponseDto {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("team_name")
    private String teamName;
    @JsonProperty("is_active")
    private Boolean isActive;
}
