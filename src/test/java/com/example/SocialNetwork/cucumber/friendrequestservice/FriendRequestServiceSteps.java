package com.example.SocialNetwork.cucumber.friendrequestservice;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.exceptions.NotFoundException;
import com.example.SocialNetwork.repository.*;
import com.example.SocialNetwork.service.EmailService;
import com.example.SocialNetwork.service.FriendRequestServiceImpl;
import com.example.SocialNetwork.service.PostServiceImpl;
import com.example.SocialNetwork.service.SocialGroupServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FriendRequestServiceSteps extends FriendRequestServiceConfig {

    @Autowired
    private FriendRequestServiceImpl friendRequestService;

    @Autowired
    private  FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    protected MockMvc mockMvc;

    private final ModelMapper mapper = new ModelMapper();
    protected static String token;
    protected static Long userOneId;
    protected static Long userTwoId;

    @Given("User one and User two exist")
    public void user_one_and_user_two_exist() {
        try {
            User userOne = User.builder()
                    .username("TestUser1")
                    .email("test_user_1@gmail.com")
                    .password(passwordEncoder.encode("testuser1"))
                    .active(true)
                    .build();

            userRepository.saveAndFlush(userOne);

            User userTwo = User.builder()
                    .username("TestUser2")
                    .email("test_user_2@gmail.com")
                    .password(passwordEncoder.encode("testuser2"))
                    .active(true)
                    .build();

            userRepository.saveAndFlush(userTwo);

            User userOneTest = userRepository.findById(userOne.getId()).orElseThrow(() -> new NotFoundException("Test user one has not been successfully saved."));
            assertNotNull(userOneTest);
            assertEquals(userOneTest.getEmail(), userOne.getEmail());

            User userTwoTest = userRepository.findById(userTwo.getId()).orElseThrow(() -> new NotFoundException("Test user two has not been successfully saved."));
            assertNotNull(userTwoTest);
            assertEquals(userTwoTest.getEmail(), userTwo.getEmail());

            userOneId = userOne.getId();
            userTwoId = userTwo.getId();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Given("User one is logged in")
    public void user_one_is_logged_in() {
        try {
            MvcResult mvcResult = mockMvc.perform(
                            post("/users/login")
                                    .contentType("application/json")
                                    .content(
                                            """
                                                            {
                                                              "email": "test_user_1@gmail.com",
                                                              "password": "testuser1"
                                                            }
                                                            """))
                    .andExpect(status().isOk())
                    .andReturn();

            token = mvcResult.getResponse().getContentAsString().substring(13).split("\"")[0];
            assertNotNull(token);
            assertNotEquals("", token);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("User one makes a friend request to User two")
    public void user_one_makes_a_friend_request_to_user_two() throws JsonProcessingException {
        FriendRequest friendRequest = FriendRequest.builder()
                .id_user2(userTwoId)
                .status(RequestStatus.PENDING)
                .build();

        String body = new ObjectMapper().writeValueAsString(friendRequest);

        try {
            MvcResult mvcResult = mockMvc.perform(post("/users/friendrequests")
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .header("Access-Control-Allow-Origin", "*")
                            .content(body))
                            .andExpect(status().is(200))
                            .andReturn();

            assertEquals("Friend request sent", mvcResult.getResponse().getContentAsString());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("User two logs in")
    public void user_two_logs_in() {
        try {
            MvcResult mvcResult = mockMvc.perform(
                            post("/users/login")
                                    .contentType("application/json")
                                    .content(
                                            """
                                                            {
                                                              "email": "test_user_2@gmail.com",
                                                              "password": "testuser2"
                                                            }
                                                            """))
                    .andExpect(status().isOk())
                    .andReturn();

            token = mvcResult.getResponse().getContentAsString().substring(13).split("\"")[0];
            assertNotNull(token);
            assertNotEquals("", token);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("User two accepts User one's friend request")
    public void user_two_accepts_user_one_s_friend_request() {
        List<FriendRequest> friendRequests = friendRequestRepository.findAllByUser1Id(userOneId);

        try {
            MvcResult mvcResult = mockMvc.perform(put("/users/friendrequests/respond?id=" + friendRequests.get(0).getId() + "&status=0")
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .header("Access-Control-Allow-Origin", "*"))
                    .andExpect(status().is(200))
                    .andReturn();

            assertEquals("Friend request accepted", mvcResult.getResponse().getContentAsString());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("User one is in User two's friend list")
    public void user_one_is_in_user_two_s_friend_list() {
        try {
            MvcResult mvcResult = mockMvc.perform(get("/users/friendrequests")
                                    .header("Authorization", "Bearer " + token)
                                    .header("Content-Type", "application/json")
                                    .header("Access-Control-Allow-Origin", "*"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertNotNull(mvcResult);
            assertTrue(mvcResult.getResponse().getContentAsString().contains("\"id_user1\":" + userOneId));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }



}
