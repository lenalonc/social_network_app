package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.SocialGroupDTO;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SocialGroupService {
    void saveGroup(SocialGroup socialGroup);

    List<SocialGroupDTO> getAllSocialGroups();

    SocialGroupDTO createGroup(SocialGroup group);

    List<SocialGroupDTO> getSocialGroupByName(String name);

    void deleteSocialGroupById(Long id, User currentUser);

    SocialGroupDTO getSocialGroupDTOById(Long id);


    SocialGroup getSocialGroupById(Long id);

    void changeGroupName(Long id, String name);
}
