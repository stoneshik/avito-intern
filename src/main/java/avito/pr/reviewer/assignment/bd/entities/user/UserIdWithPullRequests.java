package avito.pr.reviewer.assignment.bd.entities.user;

import java.util.ArrayList;
import java.util.List;

import avito.pr.reviewer.assignment.bd.entities.pr.PullRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIdWithPullRequests {
    private String userId;
    @Builder.Default
    private List<PullRequest> pullRequests = new ArrayList<>();
}
