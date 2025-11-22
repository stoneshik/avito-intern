package avito.pr.reviewer.assignment;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.script.ScriptException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

class PullRequestControllerTest extends SpringBootApplicationTest{
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
    void createPullRequest_ReturnsResponsePullRequestWithStatusCreated() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-1001",
                    "pull_request_name": "Add search",
                    "author_id": "u2"
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isCreated(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "pull_request_id": "pr-1001",
                    "pull_request_name": "Add search",
                    "author_id": "u2",
                    "status": "OPEN",
                    "assigned_reviewers": ["u1", "u3"]
                }
            """)
        );
    }

    @Test
    void createPullRequest_ReturnsResponseErrorWithStatusNotFound() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-1001",
                    "pull_request_name": "Add search",
                    "author_id": "u100"
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
    void createPullRequest_ReturnsResponseErrorWithStatusConflict() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-1000",
                    "pull_request_name": "Add search",
                    "author_id": "u1"
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isConflict(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "code": "PR_EXISTS",
                    "message": "PR id already exists"
                }
            """)
        );
    }

    @Test
    void mergeRequest_ReturnsResponsePullRequestWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests/merge")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-1000"
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            jsonPath("$.pull_request_id").value("pr-1000"),
            jsonPath("$.pull_request_name").value("Add integration tests"),
            jsonPath("$.author_id").value("u1"),
            jsonPath("$.status").value("MERGED"),
            jsonPath("$.assigned_reviewers[0]").value("u3"),
            jsonPath("$.assigned_reviewers[1]").value("u2")
        );
    }

    @Test
    void mergeRequest_ReturnsResponseErrorWithStatusNotFound() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests/merge")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-9999"
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
    void reassignRequest_ReturnsResponsePullRequestWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests/reassign")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-1000",
                    "old_reviewer_id": "u2"
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "pr": {
                        "pull_request_id": "pr-1000",
                        "pull_request_name": "Add integration tests",
                        "author_id": "u1",
                        "status": "OPEN",
                        "assigned_reviewers": ["u1", "u3"]
                    },
                    "replaced_by": "u1"
                }
            """)
        );
    }

    @Test
    void reassignRequest_ReturnsResponseErrorWithStatusNotFound() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests/reassign")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-9999",
                    "old_reviewer_id": "u1"
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
    void reassignRequest_ReturnsResponseErrorPrMergedWithStatusConflict() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests/reassign")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-999",
                    "old_reviewer_id": "u2"
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isConflict(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "code": "PR_MERGED",
                    "message": "cannot reassign on merged PR"
                }
            """)
        );
    }

    @Test
    void reassignRequest_ReturnsResponseErrorNotAssignedWithStatusConflict() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests/reassign")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-1000",
                    "old_reviewer_id": "u1"
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isConflict(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "code": "NOT_ASSIGNED",
                    "message": "reviewer is not assigned to this PR"
                }
            """)
        );
    }

    @Test
    void reassignRequest_ReturnsResponseErrorNoCandidateWithStatusConflict() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post(INITIAL_PATH + "/pull-requests/reassign")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "pull_request_id": "pr-800",
                    "old_reviewer_id": "u4"
                }
                """
            );
        this.mockMvc.perform(requestBuilder)
        .andExpectAll(
            status().isConflict(),
            content().contentTypeCompatibleWith("application/json"),
            content().json("""
                {
                    "code": "NO_CANDIDATE",
                    "message": "no active replacement candidate in team"
                }
            """)
        );
    }
}
