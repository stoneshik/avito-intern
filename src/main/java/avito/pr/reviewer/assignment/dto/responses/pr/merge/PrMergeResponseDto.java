package avito.pr.reviewer.assignment.dto.responses.pr.merge;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrMergeResponseDto {
    @JsonProperty("pull_request_id")
    private String pullRequestId;

    @JsonProperty("pull_request_name")
    private String pullRequestName;

    @JsonProperty("author_id")
    private String authorId;

    @JsonProperty("status")
    private PrStatusType status;

    @JsonProperty("assigned_reviewers")
    private List<String> assignedReviewers;
}
