package avito.pr.reviewer.assignment.bd.entities.pullrequest;

import java.time.LocalDateTime;

import avito.pr.reviewer.assignment.bd.entities.PullRequestStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequest {
    private String pullRequestId;
    private String pullRequestName;
    private String authorId;
    private PullRequestStatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime mergedAt;
}
