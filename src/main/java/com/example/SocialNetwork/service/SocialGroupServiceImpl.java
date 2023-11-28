package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SocialGroupServiceImpl implements SocialGroupService{
    private ModelMapper mapper;

    private SocialGroupRepository groupRepository;

    public SocialGroupServiceImpl(SocialGroupRepository groupRepository,ModelMapper mapper){
        this.groupRepository = groupRepository;
        this.mapper=mapper;
    }
    @Override
    public void saveGroup(SocialGroup socialGroup) {
        groupRepository.save(socialGroup);
    }

    @Override
    public List<SocialGroupDTO> getAllSocialGroups() {
        List<SocialGroup> groups=groupRepository.findAll();
        return groups.stream().map(social_group->mapper.map(social_group, SocialGroupDTO.class)).toList();
    }

    @Override
    public List<SocialGroupDTO> getSocialGroupByName(String name) {
        List<SocialGroupDTO> groups = getAllSocialGroups();
        List<SocialGroupDTO> socialGroups = new ArrayList<>();
        for (SocialGroupDTO g : groups) {
            if(g.getName().equals(name)){
                socialGroups.add(g);
            }
        }
        return socialGroups;
    }

    @Override
    public void deleteSocialGroupById(Long id) {
        SocialGroup temGroup = groupRepository.findById(id).get();
        if(temGroup!=null){
            groupRepository.deleteById(id);
        }
    }

    @Override
    public SocialGroupDTO getSocialGroupDTOById(Long id) {
        Optional<SocialGroup> socialGroup = groupRepository.findById(id);
        if(socialGroup.isPresent()){
            return socialGroup.stream().map(social_group->mapper.map(social_group, SocialGroupDTO.class)).toList().get(0);
        }
        return null;
    }

    @Override
    public SocialGroup getSocialGroupById(Long id) {
        Optional<SocialGroup> socialGroup = groupRepository.findById(id);
        if(socialGroup.isPresent()){
            return socialGroup.get();
        }
        return null;
    }
}
