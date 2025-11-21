package avito.pr.reviewer.assignment.bd.entities.team;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamWithMembers {
    private String teamName;

    @Builder.Default
    private List<Member> members = new ArrayList<>();
}
