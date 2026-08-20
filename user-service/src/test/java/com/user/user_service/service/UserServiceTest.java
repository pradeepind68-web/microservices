package com.user.user_service.service;

import com.user.user_service.dto.Payload;
import com.user.user_service.dto.UserDTO;
import com.user.user_service.entity.UserDetails;
import com.user.user_service.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(
                1,
                "Pradeep",
                "pradeep@test.com",
                "pradeep123",
                "password"
        );

        userDetails = UserDetails.builder()
                .userId(1)
                .name("Pradeep")
                .email("pradeep@test.com")
                .username("pradeep123")
                .password("password")
                .build();
    }

    @Test
    void saveUser_ShouldSaveUser_WhenUserDoesNotExist() {

        when(userRepo.existsByNameIgnoreCaseAndEmailIgnoreCase(
                anyString(), anyString()))
                .thenReturn(false);

        Payload result = userService.saveUser(userDTO);

        assertNotNull(result);
        assertEquals("User Saved Successfully ", result.response());
        assertNull(result.message());

        verify(userRepo, times(1)).save(any(UserDetails.class));
    }

    @Test
    void saveUser_ShouldReturnError_WhenUserAlreadyExists() {

        when(userRepo.existsByNameIgnoreCaseAndEmailIgnoreCase(
                anyString(), anyString()))
                .thenReturn(true);

        Payload result = userService.saveUser(userDTO);

        assertNotNull(result);
        assertNull(result.response());
        assertEquals(
                "User with same name and email already exists",
                result.message()
        );

        verify(userRepo, never()).save(any());
    }

    @Test
    void saveAllUser_ShouldSaveAllUsers_WhenNoUserExists() {

        UserDTO user1 = new UserDTO(
                1,
                "John",
                "john@test.com",
                "john",
                "pass1"
        );

        UserDTO user2 = new UserDTO(
                2,
                "Mike",
                "mike@test.com",
                "mike",
                "pass2"
        );

        when(userRepo.existsByNameIgnoreCaseAndEmailIgnoreCase(anyString(), anyString()))
                .thenReturn(false);

        Payload result = userService.saveAllUser(List.of(user1, user2));

        assertEquals("Users Saved Successfully ", result.response());
        assertNull(result.message());

        verify(userRepo, times(1)).saveAll(anyList());
    }
}
