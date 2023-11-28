package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SocialGroupServiceImpl implements SocialGroupService{

    private final SocialGroupRepository groupRepository;

    public SocialGroupServiceImpl(SocialGroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }
    @Override
    public void saveGroup(SocialGroup socialGroup) {
        groupRepository.save(socialGroup);
    }

    @Override
    public List<SocialGroup> getAllSocialGroups() {
        return groupRepository.findAll();
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

    @Override
    public void deleteSocialGroupById(Long id) {
        SocialGroup temGroup = groupRepository.findById(id).get();
        groupRepository.deleteById(id);
    }

    @Override
    public SocialGroup getSocialGroupById(Long id) {
        Optional<SocialGroup> socialGroup = groupRepository.findById(id);
        return socialGroup.orElse(null);
    }
}
