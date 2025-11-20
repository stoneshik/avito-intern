package avito.pr.reviewer.assignment.services.user;

import java.util.ArrayList;
import java.util.List;

import avito.pr.reviewer.assignment.bd.entities.pullrequest.PullRequest;
import avito.pr.reviewer.assignment.bd.entities.user.UserEntity;
import avito.pr.reviewer.assignment.bd.entities.user.UserIdWithPullRequests;
import avito.pr.reviewer.assignment.dto.responses.user.UserGetReviewResponseDto;
import avito.pr.reviewer.assignment.dto.responses.user.UserSetIsActivePullRequestResponseDto;
import avito.pr.reviewer.assignment.dto.responses.user.UserSetIsActiveResponseDto;

public class UserMapper {
    private UserMapper() {}

    public static UserSetIsActiveResponseDto fromUserEntityToUserSetIsActiveResponseDto(UserEntity user) {
        return UserSetIsActiveResponseDto.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .teamName(user.getTeamName())
            .isActive(user.getIsActive())
            .build();
    }

    public static UserGetReviewResponseDto fromUserEntityToUserGetReviewResponseDto(
        UserIdWithPullRequests userWithPullRequest
    ) {
        List<UserSetIsActivePullRequestResponseDto> pullRequests = new ArrayList<>();
        for (PullRequest pullRequest: userWithPullRequest.getPullRequests()) {
            pullRequests.add(
                UserSetIsActivePullRequestResponseDto.builder()
                    .pullRequestId(pullRequest.getPullRequestId())
                    .pullRequestName(pullRequest.getPullRequestName())
                    .authorId(pullRequest.getAuthorId())
                    .status(pullRequest.getStatus())
                    .build()
            );
        }
        return UserGetReviewResponseDto.builder()
            .userId(userWithPullRequest.getUserId())
            .pullRequests(pullRequests)
            .build();
    }
}
