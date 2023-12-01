package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.helper.MyRequest;

import java.util.List;

public interface GroupMemberService {
    public GroupMember saveGroupMember(Long id);

    List<UserDTO> getAllGroupMembers(Long id);

    void deleteGroupMemberByUserId(Long id);

    void deleteAllGroupMembers(Long id);
}
