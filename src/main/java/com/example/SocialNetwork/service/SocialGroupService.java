package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.SocialGroup;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SocialGroupService {
    void saveGroup(SocialGroup socialGroup);

    List<SocialGroupDTO> getAllSocialGroups();

    ResponseEntity<String> createGroup(SocialGroup group);

    ResponseEntity<List<SocialGroupDTO>> getSocialGroupByName(String name);

    ResponseEntity<String> deleteSocialGroupById(Long id);

    ResponseEntity<SocialGroupDTO> getSocialGroupDTOById(Long id);


    SocialGroup getSocialGroupById(Long id);

}
