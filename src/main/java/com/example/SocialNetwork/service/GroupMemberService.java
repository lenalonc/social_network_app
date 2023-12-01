package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.entities.GroupMember;

import java.util.List;

public interface GroupMemberService {
    public GroupMember saveGroupMember(Long id);

    List<UserDTO> getAllGroupMembers(Long id);

    void removeCurrentUserFromGroupByGroupId(Long id);

    void deleteAllGroupMembers(Long id);

    void removeUserFromGroupByUserID(Long id, Long groupId);
}
