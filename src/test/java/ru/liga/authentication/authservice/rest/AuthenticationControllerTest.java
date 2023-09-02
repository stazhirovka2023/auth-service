package ru.liga.authentication.authservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.liga.authentication.authservice.dto.RegDto;
import ru.liga.authentication.authservice.service.UserService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userServiceTest;

    @Autowired
    private ObjectMapper objectMapperTest;

    @Test
    void createUser_success() throws Exception {
        RegDto request = new RegDto("java_spring_test_user", "java_spring_test_user");
        given(userServiceTest.createUser(request)).willReturn(ResponseEntity.ok("Success"));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapperTest.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }
}
