package ru.liga.authentication.authservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.liga.authentication.authservice.dto.RegDto;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

    @MockBean
    private UserService userServiceTest;

    @MockBean
    private JdbcUserDetailsManager  userDetailsServiceTest;

    @Test
    void deleteUser_success() {
        RegDto request = new RegDto("java_spring_test_user", "java_spring_test_user");

        doNothing().when(userDetailsServiceTest).deleteUser(request.getUsername());
        userDetailsServiceTest.deleteUser(request.getUsername());
    }

    @Test
    void createUser_success() {
        when(userServiceTest.createUser(any(RegDto.class)))
                .thenReturn(new ResponseEntity<>("Пользователь успешно создан", HttpStatus.OK));

        RegDto request = new RegDto("java_spring_test_user", "java_spring_test_user");
        ResponseEntity<String> response = userServiceTest.createUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Пользователь успешно создан", response.getBody());
    }

    @Test
    public void createUser_alreadyExists() {
        when(userServiceTest.createUser(any(RegDto.class)))
                .thenReturn(new ResponseEntity<>("Пользователь с таким именем уже существует", HttpStatus.BAD_REQUEST));

        RegDto regDto = new RegDto("java_spring_test_user", "java_spring_test_user");
        ResponseEntity<String> responseEntity = userServiceTest.createUser(regDto);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals("Пользователь с таким именем уже существует", responseEntity.getBody());
    }
}
