package avito.pr.reviewer.assignment.dto.responses.team.create;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateResponseDto {
    @JsonProperty("team_name")
    private String teamName;

    @Builder.Default
    @JsonProperty("members")
    private List<MemberInTeamCreateResponseDto> members = new ArrayList<>();
}
