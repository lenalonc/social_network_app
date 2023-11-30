package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.GroupMember;

import java.util.List;

public interface GroupMemberService {
    public void saveGroupMember(GroupMember groupMember);

    List<Long> getAllGroupMembers(Long id);

    void deleteGroupMemberById(Long id);

    void deleteGroupMemberByUserId(Long id);
}
