package com.example.addressbook.securityconfig.service;

import com.example.addressbook.securityconfig.entity.User;
import com.example.addressbook.securityconfig.jwt.JwtProvider;
import com.example.addressbook.securityconfig.repository.RoleRepository;
import com.example.addressbook.securityconfig.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username  username
     * @param password  password
     * @return Optional of the Java Web Token, empty otherwise
     */
    public Optional<String> signin(String username, String password) {
        LOGGER.info("New user attempting to sign in");
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            try {
                //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.createToken(username, user.get().getRoles()));
            } catch (AuthenticationException e){
                LOGGER.info("Log in failed for user {}", username);
            }
        }
        return token;
    }

    /*public Optional<User> signup(String username, String password, String firstName, String lastName) {
        LOGGER.info("New user attempting to sign in");
        Optional<User> user = Optional.empty();
        if (!userRepository.findByEmail(username).isPresent()) {
            Optional<Role> role = roleRepository.findByName("ROLE_CSR");
            user = Optional.of(userRepository.save(new User(username,
                            passwordEncoder.encode(password),
                            role.get(),
                            firstName,
                            lastName)));
        }
        return user;
    }*/

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
}