package avito.pr.reviewer.assignment.dto.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import avito.pr.reviewer.assignment.bd.entities.PullRequestStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSetIsActivePullRequestResponseDto {
    @JsonProperty("pull_request_id")
    private String pullRequestId;
    @JsonProperty("pull_request_name")
    private String pullRequestName;
    @JsonProperty("author_id")
    private String authorId;
    @JsonProperty("status")
    private PullRequestStatusType status;
}
