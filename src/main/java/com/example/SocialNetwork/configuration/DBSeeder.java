package com.example.SocialNetwork.configuration;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {


    UserRepository userRepository;
    MembershipRequestRepository membershipRequestRepository;
    FriendsRepository friendsRepository;
    FriendRequestRepository friendRequestRepository;
    SocialGroupRepository socialGroupRepository;


    DBSeeder(UserRepository userRepository, MembershipRequestRepository membershipRequestRepository, SocialGroupRepository socialGroupRepository, FriendsRepository friendsRepository, FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.membershipRequestRepository = membershipRequestRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.friendsRepository = friendsRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    private void seedUser(String username, String email, String password, boolean active, boolean admin) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setActive(active);
        user.setAdmin(admin);

        userRepository.save(user);
    }

    private void seedSocialGroup(String name, boolean type) {
        SocialGroup socialGroup = new SocialGroup();
        socialGroup.setName(name);
        socialGroup.setType(type);
        List<User> adminUser = userRepository.findAll();
        socialGroup.setUser(adminUser.get(0));

        socialGroupRepository.save(socialGroup);
    }

    private void seedMembershipRequest(RequestStatus status, Long userId, Long groupId) {
        List<User> users = userRepository.findAll();
        List<SocialGroup> socialGroups = socialGroupRepository.findAll();
        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setRequestStatus(status);
        membershipRequest.setUser(users.get(2));
        membershipRequest.setSocialGroup(socialGroups.get(3));

        membershipRequestRepository.save(membershipRequest);
    }

    private void seedFriendRequest(RequestStatus requestStatus, Long user1Id, Long user2Id, Date date) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setStatus(requestStatus);
        friendRequest.setDate(date);
        friendRequest.setId_user1(user1Id);
        friendRequest.setId_user2(user2Id);
        friendRequestRepository.save(friendRequest);
    }

    private void seedFriends(Long user1Id, Long user2Id) {
        List<User> users = userRepository.findAll();
        Friends friends = new Friends();
        friends.setUser1Id(users.get(user1Id.intValue()));
        friends.setUser2Id(users.get(user2Id.intValue()));
        friendsRepository.save(friends);
    }


    @Override
    public void run(String... args) throws Exception {
        clearDatabase();

        seedUser("John", "john@example.com", "password1", true, true);
        seedUser("Alice", "alice@example.com", "password2", true, false);
        seedUser("Bob", "bob@example.com", "password3", true, false);
        seedUser("Eva", "eva@example.com", "password4", false, false);
        seedUser("Michael", "michael@example.com", "password5", false, false);

        seedSocialGroup("Group 1", true);
        seedSocialGroup("Group 2", false);
        seedSocialGroup("Group 3", true);
        seedSocialGroup("Group 4", false);
        seedSocialGroup("Group 5", true);

        seedMembershipRequest(RequestStatus.PENDING, 1L, 3L);
        seedMembershipRequest(RequestStatus.ACCEPTED, 1L, 2L);
        seedMembershipRequest(RequestStatus.PENDING, 2L, 3L);
        seedMembershipRequest(RequestStatus.REJECTED, 2L, 2L);
        seedMembershipRequest(RequestStatus.ACCEPTED, 2L, 4L);

       //seedFriendRequest(RequestStatus.PENDING, 254L, 255L, new Date(System.currentTimeMillis()));
/*     seedFriendRequest(RequestStatus.ACCEPTED, 94L, 97L, new Date(System.currentTimeMillis()));
       seedFriendRequest(RequestStatus.PENDING, 94L, 98L, new Date(System.currentTimeMillis()));
*/
        seedFriends(2L, 1L);
        seedFriends(1L, 3L);
        seedFriends(2L, 3L);
    }

    private void clearDatabase() {
        this.socialGroupRepository.deleteAll();
        this.membershipRequestRepository.deleteAll();
        this.userRepository.deleteAll();
        this.friendsRepository.deleteAll();
        this.friendRequestRepository.deleteAll();
    }
}
