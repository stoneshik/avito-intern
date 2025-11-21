package avito.pr.reviewer.assignment.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import avito.pr.reviewer.assignment.bd.entities.PrStatusType;
import avito.pr.reviewer.assignment.bd.entities.pullrequest.PullRequest;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PullRequestRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

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
