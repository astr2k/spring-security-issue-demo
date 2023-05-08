package com.example.reproduce.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

/**
 * Scenario to reproduce the issue, see {@link #test_GET_PUT_GET}
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(sharedHttpSession())
                .build();
    }

    /**
     * Test scenario is the following
     * <ul>
     *     <li>Send request to the endpoint using GET http method - returns "200 OK" as expected</li>
     *     <li>Send same request but using PUT http method - returns "401 Unauthorized" as expected</li>
     *     <li>Do not login, send again same request with GET method, expected is "200 OK",
     *         but returns "401 Unauthorized"</li>
     * </ul>
     *
     * @throws Exception
     */
    @WithAnonymousUser
    @Test
    void test_GET_PUT_GET() throws Exception {

        mockMvc.perform(get("/api/v1/item/3"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/v1/item/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Test Name"
                                }
                                """))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/item/3"))
                .andExpect(status().isOk());    // Here the test fails
    }

}