package avito.pr.reviewer.assignment.services.statistic.assignment;

import org.springframework.stereotype.Service;

import avito.pr.reviewer.assignment.dto.responses.statistic.assignment.StatisticAssignmentGetNumberOfUsersResponseDto;
import avito.pr.reviewer.assignment.repositories.StatisticAssignmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticAssignmentService {
    private final StatisticAssignmentRepository statisticAssignmentRepository;

    public StatisticAssignmentGetNumberOfUsersResponseDto getNumberOfUsers() {
        Long numberOfUsers = statisticAssignmentRepository.getNumberOfUsers();
        return StatisticAssignmentGetNumberOfUsersResponseDto.builder()
            .numberOfAssignedUsers(numberOfUsers)
            .build();
    }
}
