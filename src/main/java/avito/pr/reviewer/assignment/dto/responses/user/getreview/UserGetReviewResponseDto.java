package avito.pr.reviewer.assignment.dto.responses.user.getreview;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import avito.pr.reviewer.assignment.dto.responses.user.setisactive.UserSetIsActivePullRequestResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGetReviewResponseDto {
    @JsonProperty("user_id")
    private String userId;

    @Builder.Default
    @JsonProperty("pull_requests")
    private List<UserSetIsActivePullRequestResponseDto> pullRequests = new ArrayList<>();
}
