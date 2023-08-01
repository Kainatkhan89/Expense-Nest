package com.example.expensenest.service.impl;

import com.example.expensenest.entity.User;
import com.example.expensenest.entity.UserSignIn;
import com.example.expensenest.repository.UserRepository;
import com.example.expensenest.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users.");
        return userRepository.findAll();
    }

    public User getUserByEmailAndPassword(UserSignIn userSignIn) {
        logger.info("Fetching user by email and password for email: {}", userSignIn.getEmail());
        return userRepository.getUserByEmailAndPassword(userSignIn);
    }

    @Override
    public User getUserProfile(int userId) {
        logger.info("Fetching user profile for userId: {}", userId);
        return userRepository.getUserByID(userId);
    }

    @Override
    public Boolean setUserProfile(User userprofile) {
        logger.info("Setting user profile for userId: {}", userprofile.getId());
        return userRepository.saveUserProfile(userprofile);
    }
    @Override
    public boolean updateUser(User userprofile) {
        return userRepository.saveUserProfile(userprofile);
    }

    public User findByVerificationCode(String code) {
        logger.info("Fetching user by verification code: {}", code);
        return userRepository.findByVerificationCode(code);
    }

    @Override
    public boolean setUserPassword(User user) {
        logger.info("Setting password for user: {}", user.getEmail());
        return userRepository.setUserPassword(user);
    }

    public boolean setPasswordResetVerificationCode(String code, String email) {
        logger.info("Setting password reset verification code for email: {}", email);
        return userRepository.setCode(code,email);
    }

    @Override
    public String addUser(User user) {
        logger.info("Adding new user with email: {}", user.getEmail());
        if(userRepository.checkUserExists(user)) {
            logger.warn("User with email {} already exists.", user.getEmail());
            return null;
        } else {
            String code = getVerificationCode();
            userRepository.save(user, code);
            logger.info("New user with email {} added successfully.", user.getEmail());
            return code;
        }
    }

    @Override
    public String addSeller(User user) {
        logger.info("Adding new seller with email: {}", user.getEmail());
        if(userRepository.checkUserExists(user)) {
            logger.warn("Seller with email {} already exists.", user.getEmail());
            return null;
        } else {
            String code = getVerificationCode();
            userRepository.saveSeller(user, code);
            logger.info("New seller with email {} added successfully.", user.getEmail());
            return code;
        }
    }

    public String getVerificationCode() {
        String verificationCode = generateUserVerificationCode();
        logger.debug("Generated verification code: {}", verificationCode);
        return verificationCode;
    }

    public boolean verifyUser(String code) {
        logger.info("Verifying user with verification code: {}", code);
        return userRepository.verify(code);
    }

    @Override
    public String generateUserVerificationCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
