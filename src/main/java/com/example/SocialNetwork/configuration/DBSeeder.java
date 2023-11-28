package com.example.SocialNetwork.configuration;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.RequestStatus;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.MembershipRequestService;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class DBSeeder {

    private final SocialGroupService socialGroupService;
    private final UserService userService;
    private final MembershipRequestService membershipRequestService;

    public DBSeeder(SocialGroupService socialGroupService, UserService userService, MembershipRequestService membershipRequestService) {
        this.socialGroupService = socialGroupService;
        this.userService = userService;
        this.membershipRequestService = membershipRequestService;
    }

    public void seedDatabase() {
        seedUser("John", "john@example.com", "password1", true, true);
        seedUser("Alice", "alice@example.com", "password2", true, false);
        seedUser("Bob", "bob@example.com", "password3", true, false);
        seedUser("Eva", "eva@example.com", "password4", false, false);
        seedUser("Michael", "michael@example.com", "password5", false, false);

        seedSocialGroup("Group 1", true, 1L);
        seedSocialGroup("Group 2", false, 1L);
        seedSocialGroup("Group 3", true, 2L);
        seedSocialGroup("Group 4", false, 3L);
        seedSocialGroup("Group 5", true, 2L);

        seedMembershipRequest(RequestStatus.PENDING, 1L, 3L);
        seedMembershipRequest(RequestStatus.ACCEPTED, 1L,2L);
        seedMembershipRequest(RequestStatus.PENDING, 2L,3L);
        seedMembershipRequest(RequestStatus.REJECTED, 2L, 2L);
        seedMembershipRequest(RequestStatus.ACCEPTED, 2L, 4L);
    }

    private void seedUser(String username, String email, String password, boolean active, boolean admin) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setActive(active);
        user.setAdmin(admin);

        userService.saveUser(user);
    }

    private void seedSocialGroup(String name, boolean type, Long id_admin) {
        SocialGroup socialGroup = new SocialGroup();
        socialGroup.setName(name);
        socialGroup.setType(type);

        User adminUser = userService.findByID(id_admin);
        if (adminUser != null) {
            socialGroup.setUser(adminUser);
        }

        socialGroupService.saveGroup(socialGroup);
    }

    private void seedMembershipRequest(RequestStatus status, Long userId, Long groupId) {
        User user = userService.findByID(userId);
        SocialGroup socialGroup = socialGroupService.getSocialGroupById(groupId);

        if (user != null && socialGroup != null) {
            MembershipRequest membershipRequest = new MembershipRequest();
            membershipRequest.setRequestStatus(status);
            membershipRequest.setUser(user);
            membershipRequest.setSocialGroup(socialGroup);

            membershipRequestService.saveRequest(membershipRequest);
        }
    }



}
