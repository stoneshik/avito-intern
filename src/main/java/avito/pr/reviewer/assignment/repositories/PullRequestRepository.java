package avito.pr.reviewer.assignment.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
import avito.pr.reviewer.assignment.bd.entities.pr.PullRequest;
import avito.pr.reviewer.assignment.exceptions.NotFoundResourceError;
import avito.pr.reviewer.assignment.exceptions.PrExistsError;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PullRequestRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void savePullRequest(
        String pullRequestId,
        String pullRequestName,
        String authorId
    ) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("pull_request_id", pullRequestId);
        parameters.addValue("pull_request_name", pullRequestName);
        parameters.addValue("author_id", authorId);
        int updated = jdbcTemplate.update(
            """
            INSERT INTO pull_requests (
                pull_request_id,
                pull_request_name,
                author_id,
                status,
                created_at,
                merged_at
            ) VALUES (
                :pull_request_id,
                :pull_request_name,
                :author_id,
                'OPEN'::status_type,
                default,
                default
            )
            ON CONFLICT (pull_request_id) DO NOTHING;
            """,
            parameters
        );
        if (updated == 0) {
            throw new PrExistsError();
        }
    }

    public void assignReviewers(List<String> assignedReviewersId, String pullRequestId) {
        List<MapSqlParameterSource> batchParams = assignedReviewersId.stream()
            .map(reviewerId -> new MapSqlParameterSource()
                .addValue("user_id", reviewerId)
                .addValue("pull_request_id", pullRequestId)
            ).toList();
        jdbcTemplate.batchUpdate(
            """
            INSERT INTO assigned_reviewers (user_id, pull_request_id)
            VALUES (:user_id, :pull_request_id);
            """,
            batchParams.toArray(new MapSqlParameterSource[0])
        );
    }

    public PullRequest findPullRequestById(String pullRequestId) {
        MapSqlParameterSource selectParameterSource = new MapSqlParameterSource();
        selectParameterSource.addValue("pull_request_id", pullRequestId);
        try {
            return jdbcTemplate.queryForObject(
                """
                SELECT
                    pull_request_id,
                    pull_request_name,
                    author_id,
                    status,
                    created_at,
                    merged_at
                FROM pull_requests
                WHERE pull_request_id = :pull_request_id;
                """,
                selectParameterSource,
                (rs, rowNum) -> {
                    Timestamp mergedAt = rs.getTimestamp("merged_at");
                    return PullRequest.builder()
                        .pullRequestId(rs.getString("pull_request_id"))
                        .pullRequestName(rs.getString("pull_request_name"))
                        .authorId(rs.getString("author_id"))
                        .status(PrStatusType.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .mergedAt((mergedAt == null)? null : mergedAt.toLocalDateTime())
                        .build();
                }
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundResourceError();
        }
    }

    public PullRequest merge(String pullRequestId) {
        MapSqlParameterSource updateParameterSource = new MapSqlParameterSource();
        updateParameterSource.addValue("pull_request_id", pullRequestId);
        int updated = jdbcTemplate.update(
            """
            UPDATE pull_requests
            SET status = 'MERGED'::status_type,
                merged_at = NOW()
            WHERE pull_request_id = :pull_request_id;
            """,
            updateParameterSource
        );
        if (updated == 0) {
            throw new NotFoundResourceError();
        }
        return findPullRequestById(pullRequestId);
    }

    public PullRequest reassign(
        String pullRequestId,
        String oldReviewerId,
        String newReviewerId
    ) {
        MapSqlParameterSource updateParameterSource = new MapSqlParameterSource();
        updateParameterSource.addValue("pull_request_id", pullRequestId);
        updateParameterSource.addValue("old_reviewer_id", oldReviewerId);
        updateParameterSource.addValue("new_reviewer_id", newReviewerId);
        int updated = jdbcTemplate.update(
            """
            UPDATE assigned_reviewers
            SET user_id = :new_reviewer_id
            WHERE pull_request_id = :pull_request_id AND user_id = :old_reviewer_id;
            """,
            updateParameterSource
        );
        if (updated == 0) {
            throw new NotFoundResourceError();
        }
        return findPullRequestById(pullRequestId);
    }

    public List<String> findAssignedReviewersIds(String pullRequestId) {
        MapSqlParameterSource selectParameterSource = new MapSqlParameterSource();
        selectParameterSource.addValue("pull_request_id", pullRequestId);
        return jdbcTemplate.query(
            """
            SELECT
                user_id
            FROM assigned_reviewers
            WHERE pull_request_id = :pull_request_id;
            """,
            selectParameterSource,
            (rs, rowNum) -> rs.getString("user_id")
        );
    }

    public List<PullRequest> getPullRequestsByAuthorId(String userId) {
        MapSqlParameterSource selectParameterSource = new MapSqlParameterSource();
        selectParameterSource.addValue("user_id", userId);
        return jdbcTemplate.query(
            """
            SELECT
                pull_request_id,
                pull_request_name,
                author_id,
                status,
                created_at,
                merged_at
            FROM pull_requests
            WHERE author_id = :user_id;
            """,
            selectParameterSource,
            (rs, rowNum) -> {
                Timestamp mergedAt = rs.getTimestamp("merged_at");
                return PullRequest.builder()
                    .pullRequestId(rs.getString("pull_request_id"))
                    .pullRequestName(rs.getString("pull_request_name"))
                    .authorId(rs.getString("author_id"))
                    .status(PrStatusType.valueOf(rs.getString("status")))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .mergedAt((mergedAt == null)? null : mergedAt.toLocalDateTime())
                    .build();
            }
        );
    }
}
