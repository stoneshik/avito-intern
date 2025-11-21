package avito.pr.reviewer.assignment.bd.entities.pr;

import java.time.LocalDateTime;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
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
    private PrStatusType status;
    private LocalDateTime createdAt;
    private LocalDateTime mergedAt;
}
