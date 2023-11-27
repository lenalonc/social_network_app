package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberServiceImpl implements GroupMemberService{

    private GroupMemberRepository memberRepository;


    public GroupMemberServiceImpl(GroupMemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    @Override
    public void saveGroupMember(GroupMember groupMember) {
        memberRepository.save(groupMember);
    }
}
