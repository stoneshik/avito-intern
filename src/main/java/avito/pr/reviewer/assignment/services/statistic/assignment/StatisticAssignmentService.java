package avito.pr.reviewer.assignment.services.statistic.assignment;

import org.springframework.stereotype.Service;

import avito.pr.reviewer.assignment.repositories.StatisticAssignmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticAssignmentService {
    private final StatisticAssignmentRepository statisticAssignmentRepository;
}
