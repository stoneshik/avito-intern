package avito.pr.reviewer.assignment.dto.requests.team;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateRequestDto {
    @NotBlank
    @JsonProperty("team_name")
    private String teamName;

    @NotNull
    @JsonProperty("members")
    private List<MemberInTeamCreateRequestDto> members;
}
