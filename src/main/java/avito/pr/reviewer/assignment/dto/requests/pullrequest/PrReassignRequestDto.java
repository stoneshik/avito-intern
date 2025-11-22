package avito.pr.reviewer.assignment.dto.requests.pullrequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrReassignRequestDto {
    @NotBlank
    @JsonProperty("pull_request_id")
    private String pullRequestId;

    @NotBlank
    @JsonProperty("old_reviewer_id")
    private String oldReviewerId;
}
