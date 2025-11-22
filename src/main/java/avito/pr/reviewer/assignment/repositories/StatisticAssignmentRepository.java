package avito.pr.reviewer.assignment.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StatisticAssignmentRepository {
    private final JdbcTemplate jdbcTemplate;

    public Long getNumberOfUsers() {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(DISTINCT user_id) FROM assigned_reviewers",
            Long.class
        );
    }
}
