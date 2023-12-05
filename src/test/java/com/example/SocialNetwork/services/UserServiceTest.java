package com.example.SocialNetwork.services;

import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.dtos.PasswordDto;
import com.example.SocialNetwork.dtos.UserCreateDto;
import com.example.SocialNetwork.dtos.UserUpdateDto;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.exceptions.ValidationException;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.EmailService;
import com.example.SocialNetwork.service.UserService;
import com.example.SocialNetwork.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepository userRepository;
    private EmailService emailService;
    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(UserRepository.class);
        this.emailService = mock(EmailService.class);
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.modelMapper = new ModelMapper();
        this.userService = new UserServiceImpl(userRepository, passwordEncoder, emailService, modelMapper);
    }

    @Test
    void getAllUsersSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("user@gmail.com")
                .username("TestUser1")
                .password(passwordEncoder.encode("user1password"))
                .active(true)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@gmail.com")
                .username("TestUser2")
                .password(passwordEncoder.encode("user2password"))
                .active(true)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        var result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void deleteUserByIdSuccessfully(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        var result = userService.deleteUserById(1L);

        assertNotNull(result);
        assertEquals(new ResponseEntity<>("User successfully deleted.", HttpStatus.OK).getStatusCode(),
                    result.getStatusCode());
        assertEquals(new ResponseEntity<>("User successfully deleted.", HttpStatus.OK).getBody(),
                    result.getBody());

    }

    @Test
    void deleteUserByIdThrowsNotFoundException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUserById(1L));

        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findUserByEmailSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("email@gmail.com")
                .username("Test")
                .active(true)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user1));

        var result = userService.findUserByEmail("email@gmail.com");

        assertEquals(1, result.getId());
        assertEquals("email@gmail.com", result.getEmail());
        assertEquals("Test", result.getUsername());
        assertTrue(result.isActive());

        verify(userRepository, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findUserByEmailThrowsNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserByEmail("email@gmail.com"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUserSuccessfully() {

        User user1 = User.builder()
                .id(1L)
                .email("email@gmail.com")
                .username("Test")
                .active(true)
                .build();

        UserCreateDto userCreateDto = new UserCreateDto("email@gmail.com", "Test", null);

        when(userRepository.saveAndFlush(any())).thenReturn(user1);

        var result = userService.createUser(userCreateDto);

        assertEquals("email@gmail.com", result.getEmail());
        assertEquals("Test", result.getUsername());
        assertTrue(result.isActive());

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUserThrowsValidationExceptionInvalidEmail() {
        User user1 = User.builder()
                .id(1L)
                .email("email@gmail.com")
                .username("Test")
                .active(true)
                .build();

        UserCreateDto userCreateDto = new UserCreateDto("emailgmail.com", "Test", null);

        when(userRepository.saveAndFlush(any())).thenReturn(user1);

        assertThrows(ValidationException.class, () -> userService.createUser(userCreateDto));
    }

    @Test
    void resetUserPasswordSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("email@gmail.com")
                .username("Test")
                .active(true)
                .secretKey("secretkey")
                .build();

        PasswordDto passwordDto = new PasswordDto("Sifra12345!", "secretkey");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        userService.resetUserPassword(passwordDto, 1L);

        assertNotNull(user1.getPassword());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void forgotPasswordSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("email@gmail.com")
                .username("Test")
                .active(true)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        userService.forgotPassword("email@gmail.com");

        assertNotNull(user1.getSecretKey());

        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void forgotPasswordThrowsNotFoundException() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.forgotPassword("email@gmail.com"));

        verify(userRepository, times(1)).findByEmail(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }



    @Test
    void updateUserSuccessfully() {
        User user1 = User.builder()
                .id(1L)
                .email("email@gmail.com")
                .username("Test")
                .active(true)
                .build();

        Date date = new Date(2023, 12, 12, 12, 12, 12);
        UserUpdateDto userUpdateDto = new UserUpdateDto("novi_email@gmail.com", "TestTest", date);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);

        var result = userService.updateUser(1L, userUpdateDto);

        assertEquals("novi_email@gmail.com", result.getEmail());
        assertEquals("TestTest", result.getUsername());
        assertEquals(date, result.getDoNotDisturb());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUserThrowsNotFoundException() {
        Date date = new Date(2023, 12, 12, 12, 12, 12);
        UserUpdateDto userUpdateDto = new UserUpdateDto("novi_email@gmail.com", "TestTest", date);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, userUpdateDto));

        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

}
