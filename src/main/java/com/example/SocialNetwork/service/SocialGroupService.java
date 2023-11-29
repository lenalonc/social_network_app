package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;

import java.util.List;

public interface SocialGroupService {
    void saveGroup(SocialGroup socialGroup);

    List<SocialGroupDTO> getAllSocialGroups();

    List<SocialGroupDTO> getSocialGroupByName(String name);

    void deleteSocialGroupById(Long id);

    SocialGroupDTO getSocialGroupDTOById(Long id);
    SocialGroup getSocialGroupById(Long id);
}
