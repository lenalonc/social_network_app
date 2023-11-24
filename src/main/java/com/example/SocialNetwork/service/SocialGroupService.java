package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.SocialGroup;

import java.util.List;
import java.util.Optional;

public interface SocialGroupService {
    void saveGroup(SocialGroup socialGroup);

    List<SocialGroup> getAllSocialGroups();

    List<SocialGroup> getSocialGroupByName(String name);
}
