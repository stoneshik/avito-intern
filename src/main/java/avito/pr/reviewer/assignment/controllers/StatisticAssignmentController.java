package avito.pr.reviewer.assignment.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import avito.pr.reviewer.assignment.dto.responses.statistic.assignment.StatisticAssignmentGetNumberOfUsersResponseDto;
import avito.pr.reviewer.assignment.services.statistic.assignment.StatisticAssignmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/statistics/assignments")
@RequiredArgsConstructor
public class StatisticAssignmentController {
    private final StatisticAssignmentService statisticAssignmentService;

    @GetMapping("/number-of-users")
    public ResponseEntity<StatisticAssignmentGetNumberOfUsersResponseDto> getNumberOfUsers() {
        StatisticAssignmentGetNumberOfUsersResponseDto responseDto = statisticAssignmentService.getNumberOfUsers();
        return ResponseEntity.ok(responseDto);
    }
}
