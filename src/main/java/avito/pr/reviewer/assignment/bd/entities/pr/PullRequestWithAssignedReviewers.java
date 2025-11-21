package avito.pr.reviewer.assignment.bd.entities.pr;

import java.time.LocalDateTime;
import java.util.List;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestWithAssignedReviewers {
    private String pullRequestId;
    private String pullRequestName;
    private String authorId;
    private PrStatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime mergedAt;
    private List<String> assignedReviewers;
}
