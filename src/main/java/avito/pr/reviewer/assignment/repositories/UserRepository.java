package avito.pr.reviewer.assignment.repositories;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
import avito.pr.reviewer.assignment.bd.entities.pr.PullRequest;
import avito.pr.reviewer.assignment.bd.entities.user.UserEntity;
import avito.pr.reviewer.assignment.bd.entities.user.UserIdWithPullRequests;
import avito.pr.reviewer.assignment.exceptions.NotFoundResourceError;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserEntity setIsActive(String userId, Boolean isActive) {
        MapSqlParameterSource updateParameterSource = new MapSqlParameterSource();
        updateParameterSource.addValue("user_id", userId);
        updateParameterSource.addValue("is_active", isActive);
        int updated = jdbcTemplate.update(
            """
            UPDATE users SET is_active = :is_active
            WHERE user_id = :user_id;
            """,
            updateParameterSource
        );

        if (updated == 0) {
            throw new NotFoundResourceError();
        }

        MapSqlParameterSource selectParameterSource = new MapSqlParameterSource();
        selectParameterSource.addValue("user_id", userId);

        return findUserById(userId);
    }

    public UserEntity findUserById(String userId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user_id", userId);
        try {
            return jdbcTemplate.queryForObject(
                """
                SELECT
                    user_id,
                    username,
                    team_name,
                    is_active
                FROM users WHERE user_id = :user_id;
                """,
                parameterSource,
                (rs, rowNum) -> UserEntity.builder()
                    .userId(rs.getString("user_id"))
                    .username(rs.getString("username"))
                    .teamName(rs.getString("team_name"))
                    .isActive(rs.getBoolean("is_active"))
                    .build()
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundResourceError();
        }
    }

    public UserIdWithPullRequests findUserWithPullRequestByUserId(String userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", userId);
        return jdbcTemplate.query(
            """
            SELECT
                pr.pull_request_id,
                pr.pull_request_name,
                pr.author_id,
                pr.status,
                pr.created_at,
                pr.merged_at
            FROM pull_requests pr
            INNER JOIN assigned_reviewers ar
            ON pr.pull_request_id = ar.pull_request_id
            WHERE ar.user_id = :user_id;
            """,
            parameters,
            rs -> {
                List<PullRequest> pullRequests = new ArrayList<>();
                while (rs.next()) {
                    Timestamp mergedAt = rs.getTimestamp("merged_at");
                    PullRequest pullRequest = PullRequest.builder()
                        .pullRequestId(rs.getString("pull_request_id"))
                        .pullRequestName(rs.getString("pull_request_name"))
                        .authorId(rs.getString("author_id"))
                        .status(PrStatusType.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .mergedAt((mergedAt == null)? null : mergedAt.toLocalDateTime())
                        .build();
                    pullRequests.add(pullRequest);
                }
                return new UserIdWithPullRequests(userId, pullRequests);
            }
        );
    }
}
