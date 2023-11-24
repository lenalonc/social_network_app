package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SocialGroupServiceImpl implements SocialGroupService{

    private SocialGroupRepository groupRepository;

    public SocialGroupServiceImpl(SocialGroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }
    @Override
    public void saveGroup(SocialGroup socialGroup) {
        groupRepository.save(socialGroup);
    }

    @Override
    public List<SocialGroup> getAllSocialGroups() {
        List<SocialGroup> socialGroups = groupRepository.findAll();
        return socialGroups;
    }

    @Override
    public List<SocialGroup> getSocialGroupByName(String name) {
        List<SocialGroup> groups = getAllSocialGroups();
        List<SocialGroup> socialGroups = new ArrayList<>();
        for (SocialGroup g : groups) {
            if(g.getName().equals(name)){
                socialGroups.add(g);
            }
        }
        return socialGroups;
    }
}
