package avito.pr.reviewer.assignment;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.script.ScriptException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

class UserControllerTest extends SpringBootApplicationTest {
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
    void setIsActive_ReturnsResponseUserWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/users/set-is-active")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "user_id": "u2",
                    "is_active": false
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "user_id": "u2",
                    "username": "Bob",
                    "team_name": "backend",
                    "is_active": false
                }
            """)
        );
    }

    @Test
    void setIsActiveCheckIdempotency_ReturnsResponseUserWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/users/set-is-active")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "user_id": "u2",
                    "is_active": true
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "user_id": "u2",
                    "username": "Bob",
                    "team_name": "backend",
                    "is_active": true
                }
            """)
        );
    }

    @Test
    void setIsActive_ReturnsResponseErrorWithStatusNotFound() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/users/set-is-active")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "user_id": "u100",
                    "is_active": false
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isNotFound(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "code": "NOT_FOUND",
                    "message": "resource not found"
                }
            """)
        );
    }

    @Test
    void getReview_ReturnsResponseListPullRequestWithStatusOk() throws Exception {
        setupDb();
        final String userId = "u2";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(INITIAL_PATH + "/users/" + userId + "/get-review");
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "user_id": "u2",
                    "pull_requests": [
                        {
                            "pull_request_id": "pr-1000",
                            "pull_request_name": "Add integration tests",
                            "author_id": "u1",
                            "status": "OPEN"
                        }
                    ]
                }
            """)
        );
    }

    @Test
    void getReview_ReturnsResponseEmptyListPullRequestWithStatusOk() throws Exception {
        setupDb();
        final String userId = "u6";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(INITIAL_PATH + "/users/" + userId + "/get-review");
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "user_id": "u6",
                    "pull_requests": []
                }
            """)
        );
    }
}
