package com.example.expensenest.service.impl;

import com.example.expensenest.entity.User;
import com.example.expensenest.entity.UserSignIn;
import com.example.expensenest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserSignIn userSignIn;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userSignIn = new UserSignIn("jinal@gmail.com", "Admin123");
        user = new User();
        user.setId(1);
        user.setEmail("jinal@gmail.com");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    void testGetUserByEmailAndPassword() {
        when(userRepository.getUserByEmailAndPassword(userSignIn)).thenReturn(user);

        User result = userService.getUserByEmailAndPassword(userSignIn);

        assertEquals(user, result);
    }

    @Test
    void testGetUserProfile() {
        when(userRepository.getUserByID(1)).thenReturn(user);

        User result = userService.getUserProfile(1);

        assertEquals(user, result);
    }

    @Test
    void testSetUserProfile() {
        when(userRepository.saveUserProfile(user)).thenReturn(true);

        boolean result = userService.setUserProfile(user);

        assertTrue(result);
    }

    @Test
    void testAddUserWithNewUser() {
        when(userRepository.checkUserExists(user)).thenReturn(false);
        String verificationCode = UUID.randomUUID().toString();
        when(userRepository.save(user, verificationCode)).thenReturn(true);

        String result = userService.addUser(user);

        assertNotNull(result);
    }

    @Test
    void testAddUserWithExistingUser() {
        when(userRepository.checkUserExists(user)).thenReturn(true);

        String result = userService.addUser(user);

        assertNull(result);
    }

    @Test
    void testAddSellerWithNewSeller() {
        when(userRepository.checkUserExists(user)).thenReturn(false);
        String verificationCode = UUID.randomUUID().toString();
        when(userRepository.saveSeller(user, verificationCode)).thenReturn(true);

        String result = userService.addSeller(user);

        assertNotNull(result);
    }

    @Test
    void testAddSellerWithExistingSeller() {
        when(userRepository.checkUserExists(user)).thenReturn(true);

        String result = userService.addSeller(user);

        assertNull(result);
    }

    @Test
    void testGenerateUserVerificationCode() {
        String verificationCode = userService.generateUserVerificationCode();

        assertNotNull(verificationCode);
        assertEquals(36, verificationCode.length());
    }

    @Test
    void testVerifyUser() {
        String verificationCode = UUID.randomUUID().toString();
        when(userRepository.verify(verificationCode)).thenReturn(true);

        boolean result = userService.verifyUser(verificationCode);

        assertTrue(result);
    }
}
