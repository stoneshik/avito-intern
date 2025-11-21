package avito.pr.reviewer.assignment.services.pr;

import avito.pr.reviewer.assignment.bd.entities.pr.PullRequestWithAssignedReviewers;
import avito.pr.reviewer.assignment.dto.responses.pr.create.PrCreateResponseDto;

public class PrMapper {
    private PrMapper() {}

    public static PrCreateResponseDto fromPullRequestWithAssignedToPrCreateResponseDto(
        PullRequestWithAssignedReviewers pullRequestWithAssignedReviewers
    ) {
        return PrCreateResponseDto.builder()
            .pullRequestId(pullRequestWithAssignedReviewers.getPullRequestId())
            .pullRequestName(pullRequestWithAssignedReviewers.getPullRequestName())
            .authorId(pullRequestWithAssignedReviewers.getAuthorId())
            .status(pullRequestWithAssignedReviewers.getStatus())
            .assignedReviewers(pullRequestWithAssignedReviewers.getAssignedReviewers())
            .build();
    }
}
