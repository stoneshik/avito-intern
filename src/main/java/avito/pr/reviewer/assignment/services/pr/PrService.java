package avito.pr.reviewer.assignment.services.pr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
import avito.pr.reviewer.assignment.bd.entities.pr.PullRequest;
import avito.pr.reviewer.assignment.bd.entities.team.Member;
import avito.pr.reviewer.assignment.bd.entities.team.TeamWithMembers;
import avito.pr.reviewer.assignment.bd.entities.user.UserEntity;
import avito.pr.reviewer.assignment.dto.responses.pr.create.PrCreateResponseDto;
import avito.pr.reviewer.assignment.dto.responses.pr.merge.PrMergeResponseDto;
import avito.pr.reviewer.assignment.dto.responses.pr.reassign.PrReassignPrResponseDto;
import avito.pr.reviewer.assignment.dto.responses.pr.reassign.PrReassignResponseDto;
import avito.pr.reviewer.assignment.exceptions.NoCandidateError;
import avito.pr.reviewer.assignment.exceptions.NotAssignedError;
import avito.pr.reviewer.assignment.exceptions.PrMergedError;
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

    @Transactional
    public PrReassignResponseDto reassign(
        String pullRequestId,
        String oldReviewerId
    ) {
        UserEntity oldReviewer = userRepository.findUserById(oldReviewerId);
        PullRequest pullRequest = pullRequestRepository.findPullRequestById(pullRequestId);
        if (pullRequest.getStatus().equals(PrStatusType.MERGED)) {
            throw new PrMergedError();
        }
        List<String> assignedReviewersIds = pullRequestRepository.findAssignedReviewersIds(
            pullRequestId
        );
        if (!assignedReviewersIds.contains(oldReviewerId)) {
            throw new NotAssignedError();
        }
        TeamWithMembers teamWithActiveMembers = teamRepository.findTeamWithActiveMembers(
            oldReviewer.getTeamName()
        );
        List<Member> candidatesForReassign = new ArrayList<>();
        for (Member member: teamWithActiveMembers.getMembers()) {
            if (!member.getUserId().equals(oldReviewerId)) {
                candidatesForReassign.add(member);
            }
        }
        if (candidatesForReassign.isEmpty()) {
            throw new NoCandidateError();
        }
        Member newReviewer = getRandomMembers(candidatesForReassign, 1).get(0);
        String newReviewerId = newReviewer.getUserId();
        PullRequest updatedPullRequest = pullRequestRepository.reassign(
            pullRequestId,
            oldReviewerId,
            newReviewerId
        );
        List<String> updatedAssignedReviewersIds = pullRequestRepository.findAssignedReviewersIds(
            pullRequestId
        );
        return PrReassignResponseDto.builder()
            .pr(
                PrReassignPrResponseDto.builder()
                    .pullRequestId(updatedPullRequest.getPullRequestId())
                    .pullRequestName(updatedPullRequest.getPullRequestName())
                    .authorId(updatedPullRequest.getAuthorId())
                    .status(updatedPullRequest.getStatus())
                    .assignedReviewers(updatedAssignedReviewersIds)
                    .build()
            )
            .replacedBy(newReviewerId)
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
        return getRandomMembers(candidatesForAssignment, NUMBER_OF_ASSIGNED_REVIEWERS);
    }

    private List<Member> getRandomMembers(List<Member> members, int numberOfRandomMembers) {
        List<Member> shuffledMembers = new ArrayList<>(members);
        Collections.shuffle(shuffledMembers);
        return shuffledMembers.subList(0, numberOfRandomMembers).stream().toList();
    }
}
