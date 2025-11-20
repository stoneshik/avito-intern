package avito.pr.reviewer.assignment.services.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import avito.pr.reviewer.assignment.bd.entities.user.UserEntity;
import avito.pr.reviewer.assignment.bd.entities.user.UserIdWithPullRequests;
import avito.pr.reviewer.assignment.dto.responses.user.UserGetReviewResponseDto;
import avito.pr.reviewer.assignment.dto.responses.user.UserSetIsActiveResponseDto;
import avito.pr.reviewer.assignment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserSetIsActiveResponseDto setIsActive(
        String userId,
        Boolean isActive
    ) {
        UserEntity userEntity = userRepository.setIsActive(userId, isActive);
        return UserMapper.fromUserEntityToUserSetIsActiveResponseDto(userEntity);
    }

    @Transactional
    public UserGetReviewResponseDto getReview(String userId) {
        UserIdWithPullRequests userWithPullRequest = userRepository.findUserWithPullRequestByUserId(userId);
        return UserMapper.fromUserEntityToUserGetReviewResponseDto(userWithPullRequest);
    }
}
