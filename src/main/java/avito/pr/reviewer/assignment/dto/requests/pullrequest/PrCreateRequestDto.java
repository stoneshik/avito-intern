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
public class PrCreateRequestDto {
    @NotBlank
    @JsonProperty("pull_request_id")
    private String pullRequestId;

    @NotBlank
    @JsonProperty("pull_request_name")
    private String pullRequestName;

    @NotBlank
    @JsonProperty("author_id")
    private String authorId;
}
