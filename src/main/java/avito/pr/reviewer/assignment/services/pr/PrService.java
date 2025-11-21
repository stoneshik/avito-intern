package avito.pr.reviewer.assignment.services.pr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
import avito.pr.reviewer.assignment.bd.entities.pr.PullRequest;
import avito.pr.reviewer.assignment.bd.entities.pr.PullRequestWithAssignedReviewers;
import avito.pr.reviewer.assignment.bd.entities.team.Member;
import avito.pr.reviewer.assignment.bd.entities.team.TeamWithMembers;
import avito.pr.reviewer.assignment.bd.entities.user.UserEntity;
import avito.pr.reviewer.assignment.dto.responses.pr.create.PrCreateResponseDto;
import avito.pr.reviewer.assignment.dto.responses.pr.merge.PrMergeResponseDto;
import avito.pr.reviewer.assignment.repositories.PullRequestRepository;
import avito.pr.reviewer.assignment.repositories.TeamRepository;
import avito.pr.reviewer.assignment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrService {
    private static final int NUMBER_OF_ASSIGNED_REVIEWERS = 2;
    private final PullRequestRepository pullRequestRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public PrCreateResponseDto create(
        String pullRequestId,
        String pullRequestName,
        String authorId
    ) {
        UserEntity author = userRepository.findUserById(authorId);
        pullRequestRepository.savePullRequest(
            pullRequestId,
            pullRequestName,
            authorId
        );
        TeamWithMembers teamWithActiveMembers = teamRepository.findTeamWithActiveMembers(author.getTeamName());
        List<Member> assignedReviewers = getListAssignedUsersFromActiveTeamMembers(
            teamWithActiveMembers.getMembers(),
            authorId
        );
        List<String> assignedReviewersIds = assignedReviewers.stream().map(Member::getUserId).toList();
        pullRequestRepository.assignReviewers(assignedReviewersIds, pullRequestId);
        return PrCreateResponseDto.builder()
            .pullRequestId(pullRequestId)
            .pullRequestName(pullRequestName)
            .authorId(authorId)
            .status(PrStatusType.OPEN)
            .assignedReviewers(assignedReviewersIds)
            .build();
    }

    @Transactional
    public PrMergeResponseDto merge(String pullRequestId) {
        PullRequest pullRequest = pullRequestRepository.merge(pullRequestId);
        List<String> assignedReviewersIds = pullRequestRepository.findAssignedReviewersIds(
            pullRequest.getPullRequestId()
        );
        return PrMergeResponseDto.builder()
            .pullRequestId(pullRequest.getPullRequestId())
            .pullRequestName(pullRequest.getPullRequestName())
            .authorId(pullRequest.getAuthorId())
            .status(pullRequest.getStatus())
            .assignedReviewers(assignedReviewersIds)
            .build();
    }

    private List<Member> getListAssignedUsersFromActiveTeamMembers(List<Member> activeMembers, String authorId) {
        List<Member> candidatesForAssignment = new ArrayList<>();
        for (Member member: activeMembers) {
            if (!member.getUserId().equals(authorId)) {
                candidatesForAssignment.add(member);
            }
        }
        if (candidatesForAssignment.size() <= NUMBER_OF_ASSIGNED_REVIEWERS) {
            return candidatesForAssignment;
        }
        List<Member> shuffledMembers = new ArrayList<>(candidatesForAssignment);
        Collections.shuffle(shuffledMembers);
        return shuffledMembers.subList(0, 2).stream().toList();
    }
}
