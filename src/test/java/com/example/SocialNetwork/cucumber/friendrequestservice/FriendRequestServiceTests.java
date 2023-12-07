package com.example.SocialNetwork.cucumber.friendrequestservice;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/friend-request-service.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.SocialNetwork.cucumber.friendrequestservice")
public class FriendRequestServiceTests {
}
