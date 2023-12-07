Feature: Friend request service feature

  This service is used for all friend-request-related features.

  Scenario: User becomes a friend with another user
    Given User one and User two exist
    And User one is logged in
    When User one makes a friend request to User two
    Then User two logs in
    And User two accepts User one's friend request
    And User one is in User two's friend list