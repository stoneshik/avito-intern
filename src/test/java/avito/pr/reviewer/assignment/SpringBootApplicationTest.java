package avito.pr.reviewer.assignment;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@AutoConfigureMockMvc
abstract class SpringBootApplicationTest {
    private static final String DATABASE_NAME = "pr_reviewer_assignment_service";

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgresSqlContainer =
        new PostgreSQLContainer<>("postgres:18.1")
            .withReuse(false)
            .withDatabaseName(DATABASE_NAME);

	@Autowired
    protected MockMvc mockMvc;

    protected void setupDb() throws ScriptException {
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgresSqlContainer, "");
        ScriptUtils.executeDatabaseScript(containerDelegate, "",
            """
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
            """
        );
    }
}
