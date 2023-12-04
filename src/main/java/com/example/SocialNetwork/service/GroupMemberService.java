package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dtos.GroupMemberDTO;
import com.example.SocialNetwork.dtos.UserDTO;
import com.example.SocialNetwork.entities.GroupMember;

import java.util.List;

public interface GroupMemberService {
    GroupMemberDTO saveGroupMember(Long id);

    List<UserDTO> getAllGroupMembers(Long id);

    void removeCurrentUserFromGroupByGroupId(Long id);

    void deleteAllGroupMembers(Long id);

    void removeUserFromGroupByUserID(Long id, Long groupId);
}
