package com.example.SocialNetwork.configuration;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.repository.*;
import com.example.SocialNetwork.service.FriendsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {


    UserRepository userRepository;
    MembershipRequestRepository membershipRequestRepository;
    SocialGroupRepository socialGroupRepository;
    GroupMemberRepository groupMemberRepository;
    FriendRequestRepository friendRequestRepository;
    FriendsRepository friendsRepository;

    PostRepository postRepository;

    PasswordEncoder passwordEncoder;
    CommentRepository commentRepository;


    DBSeeder(UserRepository userRepository,
             MembershipRequestRepository membershipRequestRepository,
             SocialGroupRepository socialGroupRepository,
             GroupMemberRepository groupMemberRepository,
             PasswordEncoder passwordEncoder, FriendRequestRepository friendRequestRepository, FriendsRepository friendsRepository,
             PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.membershipRequestRepository = membershipRequestRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.friendsRepository = friendsRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.commentRepository = commentRepository;
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

    private void seedComment(String text, Date date, int id_user, int id_post) {
        List<User> users = userRepository.findAll();
        List<Post> posts = postRepository.findAll();

        Comment comment = new Comment();
        comment.setText(text);
        comment.setPost(posts.get(id_post));
        comment.setUser(users.get(id_user));
        comment.setDate(date);

        commentRepository.save(comment);

    }

    private void seedReplies(String text, Date date, int id_user, int id_comment) {
        List<User> users = userRepository.findAll();
        List<Comment> comments = commentRepository.findAll();

        Comment comment = new Comment();
        comment.setText(text);
        comment.setPost(comments.get(id_comment).getPost());
        comment.setUser(users.get(id_user));
        comment.setDate(date);
        comment.setParentComment(comments.get(id_comment));

        commentRepository.save(comment);
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

    private void seedFriendRequest(RequestStatus requestStatus, int user1Id, int user2Id, Date date) {
        List<User> users = userRepository.findAll();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setStatus(requestStatus);
        friendRequest.setDate(date);
        friendRequest.setId_user1(users.get(user1Id).getId());
        friendRequest.setId_user2(users.get(user2Id).getId());
        friendRequestRepository.save(friendRequest);
    }

    private void seedFriends(int user1Id, int user2Id) {
        List<User> users = userRepository.findAll();
        Friends friends = new Friends();
        friends.setUser1Id(users.get(user1Id));
        friends.setUser2Id(users.get(user2Id));
        friendsRepository.save(friends);
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

    private void seedPost(String text, boolean type, Date date, int id_user, int id_socialgroup) {
        List<User> users = userRepository.findAll();
        List<SocialGroup> socialGroups = socialGroupRepository.findAll();

        Post post = Post.builder()
                .text(text)
                .type(type)
                .deleted(false)
                .user(users.get(id_user))
                .date(date)
                .build();

        if (id_socialgroup >= socialGroups.size()) {
            post.setSocialGroup(null);
        } else post.setSocialGroup(socialGroups.get(id_socialgroup));

        postRepository.save(post);
    }


    @Override
    public void run(String... args) throws Exception {
        clearDatabase();

        seedUser("John", "john@example.com", "password1", true);
        seedUser("Alice", "alice@example.com", "password2", true);
        seedUser("Bob", "bob@example.com", "password3", true);
        seedUser("Eva", "eva@example.com", "password4", true);
        seedUser("Michael", "michael@example.com", "password5", false);

        seedSocialGroup("Group1", true, 3);
        seedSocialGroup("Group2", false, 0);
        seedSocialGroup("Group3", true, 0);
        seedSocialGroup("Group4", false, 0);
        seedSocialGroup("Group5", true, 1);

        seedMembershipRequest(RequestStatus.PENDING,3,0);
        seedMembershipRequest(RequestStatus.ACCEPTED,0,1);
        seedMembershipRequest(RequestStatus.PENDING,0, 2);
        seedMembershipRequest(RequestStatus.REJECTED,2,2);
        seedMembershipRequest(RequestStatus.ACCEPTED,1,4);

        seedGroupMember(0, 2);
        seedGroupMember(0, 3);
        seedGroupMember(2, 2);
        seedGroupMember(1, 2);
        seedGroupMember(3, 0);

        seedFriendRequest(RequestStatus.PENDING, 0, 1, new Date());
        seedFriendRequest(RequestStatus.ACCEPTED, 0, 2, new Date());
        seedFriendRequest(RequestStatus.PENDING, 1, 2, new Date());

        seedFriends(0, 1);
        seedFriends(0, 2);
        seedFriends(1, 2);

        seedPost("text1", true, new Date(), 0, 10);
        seedPost("text2", false, new Date(), 0, 30);
        seedPost("text3", true, new Date(), 2, 20);
        seedPost("text4", true, new Date(2023, 11, 11, 12, 12, 12), 1, 2);
        seedPost("text4", true, new Date(2023, 11, 26, 12, 12, 12), 1, 1);
        seedPost("text5", true, new Date(), 3, 0);

        seedComment("com1", new Date(), 2, 2);
        seedComment("com2", new Date(), 1, 2);
        seedComment("com3", new Date(), 1, 3);
        seedComment("com4", new Date(), 0, 3);

        seedReplies("rep1", new Date(), 0, 0);
        seedReplies("rep2", new Date(), 1, 0);
        seedReplies("rep3", new Date(), 2, 0);

    }

    private void clearDatabase() {
        this.postRepository.deleteAll();
        this.commentRepository.deleteAll();
        this.groupMemberRepository.deleteAll();
        this.membershipRequestRepository.deleteAll();
        this.socialGroupRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
