package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, User user) {
        User tempUser = userRepository.findById(id).get();

        if(tempUser != null){
            if(user.getEmail() != null && !user.getEmail().equals("") && user.getEmail() != tempUser.getEmail()) {
                tempUser.setEmail(user.getEmail());
            }
            if(user.getUsername() != null && !user.getUsername().equals("") && user.getUsername() != tempUser.getUsername()){
                tempUser.setUsername(user.getUsername());
            }
            if(user.isActive() != tempUser.isActive()){
                tempUser.setActive(user.isActive());
            }

        }

        userRepository.save(tempUser);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }

    @Override
    public void deleteUserById(Long id) {
        User tempUser = userRepository.findById(id).get();
        if(tempUser!= null) {
            userRepository.deleteById(id);
        }
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
        //TODO Izbaciti gresku kada nije pronadjen korisnik sa tim email-om
        User user = userRepository.findByEmail(email);

        if(!user.isActive()){
            //TODO Izbaciti gresku jer korisnik nije aktivan, tj. logicki je obrisan
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    public User findUserByEmail(String email) {
        //TODO Izbaciti gresku ako nema korisnika sa ovim email-om
        return userRepository.findByEmail(email);
    }

}
