package com.example.SocialNetwork.configuration;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {


    UserRepository userRepository;
    MembershipRequestRepository membershipRequestRepository;
    SocialGroupRepository socialGroupRepository;
    GroupMemberRepository groupMemberRepository;
    BCryptPasswordEncoder passwordEncoder;



    DBSeeder(UserRepository userRepository,
             MembershipRequestRepository membershipRequestRepository,
             SocialGroupRepository socialGroupRepository,
             GroupMemberRepository groupMemberRepository,
             BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.membershipRequestRepository = membershipRequestRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void seedUser(String username, String email, String password, boolean active) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        user.setActive(active);

        userRepository.save(user);
    }

    private void seedSocialGroup(String name, boolean type, int id) {
        SocialGroup socialGroup = new SocialGroup();
        socialGroup.setName(name);
        socialGroup.setType(type);
        List<User> adminUser = userRepository.findAll();
        socialGroup.setUser(adminUser.get(id));

        socialGroupRepository.save(socialGroup);
    }

    private void seedMembershipRequest(RequestStatus status, int id_user, int id_socialgroup) {
        List<User> users = userRepository.findAll();
        List<SocialGroup> socialGroups = socialGroupRepository.findAll();
        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setRequestStatus(status);
        membershipRequest.setUser(users.get(id_user));
        membershipRequest.setSocialGroup(socialGroups.get(id_socialgroup));

        membershipRequestRepository.save(membershipRequest);
    }

    private void seedGroupMember(int id_user, int id_socialgroup) {
        List<User> users = userRepository.findAll();
        List<SocialGroup> socialGroups = socialGroupRepository.findAll();
        GroupMember groupMember = new GroupMember();

        groupMember.setUser(users.get(id_user));
        groupMember.setDateJoined(new Date());
        groupMember.setSocialGroup(socialGroups.get(id_socialgroup));

        groupMemberRepository.save(groupMember);
    }


    @Override
    public void run(String... args) throws Exception {
        clearDatabase();

        seedUser("John", "john@example.com", "password1", true);
        seedUser("Alice", "alice@example.com", "password2", true);
        seedUser("Bob", "bob@example.com", "password3", true);
        seedUser("Eva", "eva@example.com", "password4", false);
        seedUser("Michael", "michael@example.com", "password5", false);

        seedSocialGroup("Group1", true,1);
        seedSocialGroup("Group2", false,1);
        seedSocialGroup("Group3", true,2);
        seedSocialGroup("Group4", false,2);
        seedSocialGroup("Group5", true,3);

        seedMembershipRequest(RequestStatus.PENDING,1,2);
        seedMembershipRequest(RequestStatus.ACCEPTED,1,1);
        seedMembershipRequest(RequestStatus.PENDING,2, 3);
        seedMembershipRequest(RequestStatus.REJECTED,3,4);
        seedMembershipRequest(RequestStatus.ACCEPTED,1,4);

        seedGroupMember(0,1);
        seedGroupMember(1,1);
        seedGroupMember(2,3);
        seedGroupMember(1,2);
        seedGroupMember(1,3);
    }

    private void clearDatabase() {
        this.groupMemberRepository.deleteAll();
        this.membershipRequestRepository.deleteAll();
        this.socialGroupRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
