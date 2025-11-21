package avito.pr.reviewer.assignment.services.pullrequest;

import org.springframework.stereotype.Service;

import avito.pr.reviewer.assignment.repositories.PullRequestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PullRequestService {
    private final PullRequestRepository pullRequestRepository;
}
