package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.SocialGroup;

import java.util.List;

public interface SocialGroupService {
    void saveGroup(SocialGroup socialGroup);

    List<SocialGroup> getAllSocialGroups();

    List<SocialGroup> getSocialGroupByName(String name);

    void deleteSocialGroupById(Long id);

    SocialGroup getSocialGroupById(Long id);
}
