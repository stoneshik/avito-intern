package avito.pr.reviewer.assignment;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные
        Integer teamCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM teams", Integer.class);
        if (teamCount == null || teamCount == 0) {
            setupDb();
        }
    }

    private void setupDb() throws SQLException {
        String sqlScript = """
            INSERT INTO teams (team_name)
            VALUES ('backend');
            INSERT INTO teams (team_name)
            VALUES ('empty_team');
            INSERT INTO teams (team_name)
            VALUES ('frontend');

            INSERT INTO users (user_id, username, team_name, is_active)
            VALUES (
                'u1',
                'Alice',
                'backend',
                true
            );
            INSERT INTO users (user_id, username, team_name, is_active)
            VALUES (
                'u2',
                'Bob',
                'backend',
                true
            );
            INSERT INTO users (user_id, username, team_name, is_active)
            VALUES (
                'u3',
                'Max',
                'backend',
                true
            );
            INSERT INTO users (user_id, username, team_name, is_active)
            VALUES (
                'u4',
                'Ann',
                'empty_team',
                true
            );
            INSERT INTO users (user_id, username, team_name, is_active)
            VALUES (
                'u5',
                'John',
                'empty_team',
                false
            );
            INSERT INTO users (user_id, username, team_name, is_active)
            VALUES (
                'u6',
                'John',
                'frontend',
                false
            );

            INSERT INTO pull_requests (pull_request_id, pull_request_name, author_id, status, created_at, merged_at)
            VALUES (
                'pr-800',
                'Empty pull request',
                'u5',
                'OPEN'::status_type,
                default,
                NOW()
            );
            INSERT INTO pull_requests (pull_request_id, pull_request_name, author_id, status, created_at, merged_at)
            VALUES (
                'pr-999',
                'Add docs',
                'u2',
                'MERGED'::status_type,
                default,
                NOW()
            );
            INSERT INTO pull_requests (pull_request_id, pull_request_name, author_id, status, created_at, merged_at)
            VALUES (
                'pr-1000',
                'Add integration tests',
                'u1',
                'OPEN'::status_type,
                default,
                null
            );

            INSERT INTO assigned_reviewers (user_id, pull_request_id)
            VALUES (
                'u4',
                'pr-800'
            );

            INSERT INTO assigned_reviewers (user_id, pull_request_id)
            VALUES (
                'u1',
                'pr-999'
            );
            INSERT INTO assigned_reviewers (user_id, pull_request_id)
            VALUES (
                'u3',
                'pr-999'
            );

            INSERT INTO assigned_reviewers (user_id, pull_request_id)
            VALUES (
                'u2',
                'pr-1000'
            );
            INSERT INTO assigned_reviewers (user_id, pull_request_id)
            VALUES (
                'u3',
                'pr-1000'
            );
        """;

        try (Connection connection = dataSource.getConnection()) {
            Resource resource = new ByteArrayResource(sqlScript.getBytes(StandardCharsets.UTF_8));
            ScriptUtils.executeSqlScript(connection, resource);
        }
    }
}
