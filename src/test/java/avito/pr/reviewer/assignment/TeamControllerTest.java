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

class TeamControllerTest extends SpringBootApplicationTest {
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
    void createTeam_ReturnsResponseTeamWithStatusCreated() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/team/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "team_name": "payments",
                    "members": [
                        {
                            "user_id": "u1",
                            "username": "Alice",
                            "is_active": true
                        },
                        {
                            "user_id": "u2",
                            "username": "Bob",
                            "is_active": true
                        }
                    ]
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isCreated(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "team_name": "payments",
                    "members": [
                        {
                            "user_id": "u1",
                            "username": "Alice",
                            "is_active": true
                        },
                        {
                            "user_id": "u2",
                            "username": "Bob",
                            "is_active": true
                        }
                    ]
                }
            """)
        );
    }

    @Test
    void createTeam_ReturnsResponseErrorWithStatusBadRequest() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/team/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "team_name": "backend",
                    "members": [
                        {
                            "user_id": "u1",
                            "username": "Alice",
                            "is_active": true
                        },
                        {
                            "user_id": "u2",
                            "username": "Bob",
                            "is_active": true
                        }
                    ]
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isBadRequest(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "code": "TEAM_EXISTS",
                    "message": "team_name already exists"
                }
            """)
        );
    }

    @Test
    void getTeam_ReturnsResponseTeamWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/team/add")
            .param("team_name", "backend");
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "team_name": "backend",
                    "members": [
                        {
                            "user_id": "u1",
                            "username": "Alice",
                            "is_active": true
                        },
                        {
                            "user_id": "u2",
                            "username": "Bob",
                            "is_active": true
                        },
                        {
                            "user_id": "u3",
                            "username": "Max",
                            "is_active": true
                        }
                    ]
                }
            """)
        );
    }

    @Test
    void getTeam_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/team/add")
            .param("team_name", "payments");
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "code": "NOT_FOUND",
                    "message": "resource not found"
                }
            """)
        );
    }
}
