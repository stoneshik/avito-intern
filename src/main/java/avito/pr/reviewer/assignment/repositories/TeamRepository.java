package avito.pr.reviewer.assignment.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import avito.pr.reviewer.assignment.bd.entities.team.Member;
import avito.pr.reviewer.assignment.bd.entities.team.TeamWithMembers;
import avito.pr.reviewer.assignment.exceptions.NotFoundResourceError;
import avito.pr.reviewer.assignment.exceptions.TeamExistsError;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TeamRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TeamWithMembers saveTeamWithUsers(TeamWithMembers teamWithMembers) {
        String teamName = saveTeam(teamWithMembers.getTeamName());
        List<Member> savedMembers = saveAllMembers(teamWithMembers.getMembers(), teamName);
        return TeamWithMembers.builder()
            .teamName(teamName)
            .members(savedMembers)
            .build();
    }

    public String saveTeam(String teamName) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("team_name", teamName);
        int updated = jdbcTemplate.update(
            """
            INSERT INTO teams (team_name)
            VALUES (:team_name)
            ON CONFLICT (team_name) DO NOTHING;
            """,
            parameters
        );
        if (updated == 0) {
            throw new TeamExistsError();
        }
        return teamName;
    }

    public List<Member> saveAllMembers(List<Member> members, String teamName) {
        List<MapSqlParameterSource> batchParams = members.stream()
            .map(member -> new MapSqlParameterSource()
                .addValue("user_id", member.getUserId())
                .addValue("username", member.getUsername())
                .addValue("team_name", teamName)
                .addValue("is_active", member.getIsActive())
            ).toList();

        return jdbcTemplate.batchUpdate(
            """
            INSERT INTO users (user_id, username, team_name, is_active)
            VALUES (:user_id, :username, :team_name, :is_active)
            ON CONFLICT (user_id) DO UPDATE SET
                username = EXCLUDED.username,
                team_name = EXCLUDED.team_name,
                is_active = EXCLUDED.is_active;
            """,
            batchParams.toArray(new MapSqlParameterSource[0])
        ).length > 0 ? members : Collections.emptyList();
    }

    public TeamWithMembers findTeamWithMembers(String teamName) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("team_name", teamName);
        String teamNameFound;
        try {
            teamNameFound = jdbcTemplate.queryForObject(
                "SELECT team_name FROM teams WHERE team_name = :team_name;",
                parameters,
                (rs, rowNum) -> rs.getString("team_name")
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundResourceError();
        }
        List<Member> members = jdbcTemplate.query(
            """
            SELECT
                user_id,
                username,
                is_active
            FROM users WHERE team_name = :team_name;
            """,
            parameters,
            (rs, rowNum) -> Member.builder()
                .userId(rs.getString("user_id"))
                .username(rs.getString("username"))
                .isActive(rs.getBoolean("is_active"))
                .build()
        );
        return TeamWithMembers.builder()
            .teamName(teamNameFound)
            .members(members)
            .build();
    }
}
