package avito.pr.reviewer.assignment.dto.responses.pr.reassign;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
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
public class PrReassignPrResponseDto {
    @NotBlank
    @JsonProperty("pull_request_id")
    private String pullRequestId;

    @NotBlank
    @JsonProperty("pull_request_name")
    private String pullRequestName;

    @NotBlank
    @JsonProperty("author_id")
    private String authorId;

    @NotBlank
    @JsonProperty("status")
    private PrStatusType status;

    @NotNull
    @JsonProperty("assigned_reviewers")
    private List<String> assignedReviewers;
}
