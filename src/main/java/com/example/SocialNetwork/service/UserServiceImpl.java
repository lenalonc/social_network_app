package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.dtos.PasswordDto;
import com.example.SocialNetwork.dtos.UserCreateDto;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    @Value("${password.activate.endpoint}")
    private String passwordActivateEndpoint;

    // Email adresa mora biti u korektnom formatu: "nesto@nesto.nesto"
    private final Pattern emailPattern = Pattern.compile("^[a-z0-9_.-]+@(.+)$");

    // Lozinka mora sadrzati barem po jedno malo slovo, veliko slovo, broj, specijalni karakter i mora imati duzinu barem 8 karaktera
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,}$");


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User createUser(UserCreateDto userCreateDto) {
//        if(!emailPattern.matcher(userCreateDto.getEmail()).matches()) {
//            throw new ValidationException("invalid email");
//        }

        String secretKey = RandomStringUtils.randomNumeric(6);

        User user = User.builder()
                .email(userCreateDto.getEmail())
                .username(userCreateDto.getUsername())
                .roles(userCreateDto.getRoles())
                .active(true)
                .secretKey(RandomStringUtils.randomNumeric(6))
                .build();

        userRepository.saveAndFlush(user);

        String text = "Secret key: " + secretKey + "\n" + "Link: " + passwordActivateEndpoint + "/" + user.getId();
        emailService.sendEmail(user.getEmail(), "Activate account", text);

        return user;
    }

    public void resetUserPassword(PasswordDto passwordDto, Long id) {
//        if(!passwordPattern.matcher(passwordDto.getPassword()).matches()) {
//            throw new ValidationException("Invalid password format. Password has to contain" +
//                    " at least one of each: uppercase letter, lowercase letter, number, and special character. " +
//                    "It also has to be at least 8 characters long.");
//        }

        //TODO Ako ga ne nadje, onda se baca izuzetak da nije pronadjen u bazi
        User user = userRepository.findById(id).get();

        if(user.getSecretKey() == null || !user.getSecretKey().equals(passwordDto.getSecretKey())) {
            //TODO Baca se izuzetak za neuspesnu validaciju
        }

        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        user.setSecretKey(null);

        userRepository.save(user);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User has not been found."));

        String secretKey = RandomStringUtils.randomAlphabetic(6);
        user.setSecretKey(secretKey);
        userRepository.save(user);

        String text = "Secret key: " + secretKey + "\n" + "Link: " + passwordActivateEndpoint + "/" + user.getId();
        emailService.sendEmail(user.getEmail(), "Reset password", text);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<String> updateUser(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User tempUser = optionalUser.get();
            if(user.getEmail() != null && !user.getEmail().equals("") && user.getEmail() != tempUser.getEmail()) {
                tempUser.setEmail(user.getEmail());
            }
            if(user.getUsername() != null && !user.getUsername().equals("") && user.getUsername() != tempUser.getUsername()){
                tempUser.setUsername(user.getUsername());
            }
            if(user.isActive() != tempUser.isActive()){
                tempUser.setActive(user.isActive());
            }
            userRepository.save(tempUser);
            return new ResponseEntity<>("User updated", HttpStatus.OK);
        }

        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        ModelMapper mapper = new ModelMapper();
        List<User> users = userRepository.findAll();
        return users.stream().map(user->mapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public ResponseEntity<String> deleteUserById(Long id) {
        Optional<User> tempUser = userRepository.findById(id);
        if (tempUser.isPresent()){
            userRepository.deleteById(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public User findByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User has not been found."));

        if(!user.isActive()){
            //TODO Izbaciti gresku jer korisnik nije aktivan, tj. logicki je obrisan
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User has not been found."));
    }

    public User findCurrentUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.get();

    }
}
