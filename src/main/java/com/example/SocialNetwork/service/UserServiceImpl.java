package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.PasswordDto;
import com.example.SocialNetwork.dtos.UserCreateDto;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.dtos.UserUpdateDto;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.exceptions.ForbiddenException;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.exceptions.ValidationException;
import com.example.SocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    private ModelMapper mapper;

    @Value("${password.activate.endpoint}")
    private String passwordActivateEndpoint;

    // Email adresa mora biti u korektnom formatu: "nesto@nesto.nesto"
    private final Pattern emailPattern = Pattern.compile("^[a-z0-9_.-]+@(.+)$");

    // Lozinka mora sadrzati barem po jedno malo slovo, veliko slovo, broj, specijalni karakter i mora imati duzinu barem 8 karaktera
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,}$");


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.mapper = mapper;
    }

    public UserDTO createUser(UserCreateDto userCreateDto) {
        if(!emailPattern.matcher(userCreateDto.getEmail()).matches()) {
            throw new ValidationException("invalid email");
        }

        String secretKey = RandomStringUtils.randomNumeric(6);

        User user = User.builder()
                .email(userCreateDto.getEmail())
                .username(userCreateDto.getUsername())
                .roles(userCreateDto.getRoles())
                .active(true)
                .secretKey(secretKey)
                .build();

        userRepository.saveAndFlush(user);

        String text = "Secret key: " + secretKey + "\n" + "Link: " + passwordActivateEndpoint + "/" + user.getId();
        emailService.sendEmail(user.getEmail(), "Activate account", text);

        return mapper.map(user, UserDTO.class);
    }

    public void resetUserPassword(PasswordDto passwordDto, Long id) {
/*        if(!passwordPattern.matcher(passwordDto.getPassword()).matches()) {
            throw new ValidationException("Invalid password format. Password has to contain" +
                    " at least one of each: uppercase letter, lowercase letter, number, and special character. " +
                    "It also has to be at least 8 characters long.");
        }*/

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User has not been found."));

        if(user.getSecretKey() == null || !user.getSecretKey().equals(passwordDto.getSecretKey())) {
            throw new ValidationException("Invalid secret key.");
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
    public UserDTO updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User has not been found."));

        if(userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isBlank())
            user.setEmail(userUpdateDto.getEmail());

        if(userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().isBlank())
            user.setUsername(userUpdateDto.getUsername());

        if(userUpdateDto.getDoNotDisturb() != null)
            user.setDoNotDisturb(userUpdateDto.getDoNotDisturb());

        return mapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user->mapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public ResponseEntity<String> deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User has not been found."));
        user.setActive(false);

        return new ResponseEntity<>("User successfully deleted.", HttpStatus.OK);
    }

    @Override
    public UserDTO findByID(Long id) {
        return mapper.map(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User has not been found.")), UserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User has not been found."));

        if(!user.isActive()){
            throw new ForbiddenException("User is not active.");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User has not been found."));
    }

    public User findCurrentUser() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
    }

    @Override
    public UserDTO findByUsername(String name) {
        User user = userRepository.findByUsernameIgnoreCase(name).orElseThrow(() -> new NotFoundException("User has not been found."));
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public ResponseEntity<Object> setDoNotDisturb(int days) {
        User user = findCurrentUser();

        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, days);
        Date date = calendar.getTime();

        user.setDoNotDisturb(date);

        return new ResponseEntity<>("Do not disturb successfully set.", HttpStatus.OK);
    }

}
