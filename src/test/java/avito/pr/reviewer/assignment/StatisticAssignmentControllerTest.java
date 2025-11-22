package avito.pr.reviewer.assignment;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.script.ScriptException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

class StatisticAssignmentControllerTest extends SpringBootApplicationTest {
    @AfterEach
    void tearDownTest() throws ScriptException {
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(postgresSqlContainer, "");
        ScriptUtils.executeDatabaseScript(containerDelegate, "",
            """
            TRUNCATE assigned_reviewers CASCADE;
            TRUNCATE users CASCADE;
            TRUNCATE teams CASCADE;
            TRUNCATE pull_requests CASCADE;
            """
        );
    }

    @Test
    void getStatisticAssignedUsers_ReturnsResponseNumberAssignedUsersRequestWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(INITIAL_PATH + "/statistics/assignments/number-of-users");
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "number_of_assigned_users": 4
                }
            """)
        );
    }
}
