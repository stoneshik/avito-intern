package avito.pr.reviewer.assignment.repositories;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import avito.pr.reviewer.assignment.bd.entities.pullrequest.PullRequest;
import avito.pr.reviewer.assignment.bd.entities.user.UserEntity;
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
        selectParameterSource.addValue("user_id", selectParameterSource);

        return jdbcTemplate.queryForObject(
            """
            SELECT
                user_id,
                username,
                team_name,
                is_active
            FROM users WHERE user_id = :userId;
            """,
            selectParameterSource,
            (rs, rowNum) -> UserEntity.builder()
                .userId(rs.getString("user_id"))
                .username(rs.getString("username"))
                .teamName(rs.getString("team_name"))
                .isActive(rs.getBoolean("is_active"))
                .build()
        );
    }
}
