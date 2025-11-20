package avito.pr.reviewer.assignment;

import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
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

    @AfterAll
    void stopContainers() {
        if (postgresSqlContainer.isRunning()) {
            postgresSqlContainer.stop();
        }
    }

	@Autowired
    protected MockMvc mockMvc;
}
