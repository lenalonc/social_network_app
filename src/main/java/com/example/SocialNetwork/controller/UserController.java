package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dtos.FriendRequestDTO;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.dtos.*;
import com.example.SocialNetwork.dtos.LoginRequest;
import com.example.SocialNetwork.dtos.LoginResponse;
import com.example.SocialNetwork.dtos.PasswordDto;
import com.example.SocialNetwork.dtos.UserCreateDto;
import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendRequestService;
import com.example.SocialNetwork.service.FriendsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.UserServiceImpl;
import com.example.SocialNetwork.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final FriendRequestService friendRequestService;
    private final FriendsService friendsService;
    private final GroupMemberService groupMemberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(userService.findUserByEmail(loginRequest.getEmail()))));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.createUser(userCreateDto));
    }

    @PostMapping("/activate_password/{id}")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordDto passwordDto, @PathVariable Long id) {
        userService.resetUserPassword(passwordDto, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello Levi9 konferencijska sala uvek radi", HttpStatus.OK);
    }

    @PostMapping("/friend_requests")
    public ResponseEntity<Object> sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        friendRequest.setId_user1(getCurrentUser().getId());
        friendRequest.setDate(new Date());
        return friendRequestService.sendFriendRequest(friendRequest);
    }

    @GetMapping("/friend_requests")
    public ResponseEntity<Object> getAllRequests() {
        Long uid = getUser();
        List<FriendRequestDTO> listOfFriends = friendRequestService.getAllRequests(uid);

        if (listOfFriends.isEmpty()) {
            return ResponseEntity.ok("You have no friend requests");
        } else {
            return ResponseEntity.ok(listOfFriends);
        }
    }

    @PutMapping("/friend_requests/respond")
    public ResponseEntity<Object> respondToRequest(@RequestParam(name = "id") Long id, @RequestParam(name = "status") Long status) {
        return friendRequestService.respondToRequest(id, status);
    }

    @DeleteMapping("/friend_requests/{id}")
    public ResponseEntity<Object> deleteFriendRequest(@PathVariable Long id) {
        return friendRequestService.deleteFriendRequest(id);
    }

    @GetMapping("/friend_requests/your_requests")
    public ResponseEntity<Object> getYourRequests() {
        return friendRequestService.getYourRequests();
    }

    @GetMapping("/current_user")
    public User getCurrentUser() {
        return userService.findCurrentUser();
    }

    @PostMapping("/")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>("User saved", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDto));
    }

    @GetMapping("/")
    public ResponseEntity<?> showAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteUser() {
        Long id = getUser();
        return userService.deleteUserById(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findByID(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriendsByUser() {
        List<UserDTO> friends = friendsService.getFriendsByUser();
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/friends/search")
    public ResponseEntity<?> searchFriends(@RequestParam("value") String search) {
        return friendsService.searchFriends(search);
    }

    @DeleteMapping("/friends/admin_delete")
    public ResponseEntity<String> deleteFriendByUser(@RequestParam("user1Id") Long user1Id, @RequestParam("user2Id") Long user2Id) {
        return friendsService.deleteFriendByUser(user1Id, user2Id);
    }

    @DeleteMapping("/friends")
    public ResponseEntity<String> deleteFriend(@RequestParam("friendId") Long friendId) {
        return friendsService.deleteFriend(friendId);
    }

    @PostMapping("/set_do_not_disturb")
    public ResponseEntity<Object> setDoNotDisturb(@RequestParam("days") int days) {
        return userService.setDoNotDisturb(days);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        invalidateSession(request);

        if(authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        return new ResponseEntity<>("Logout success!" , HttpStatus.OK);
    }

    @DeleteMapping("/remove_user")
    public ResponseEntity<?> removeFromGroup(@RequestParam Long userId, @RequestParam Long groupId){
        groupMemberService.removeUserFromGroupByUserID(userId, groupId);
        return ResponseEntity.ok().build();
    }

    private void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String name) {
        UserDTO user = userService.findByUsername(name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private Long getUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long uid = user.get().getId();
        return uid;
    }

}
