package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.RequestService;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/groupmember")
public class GroupMemberController {
    private final GroupMemberService memberService;
    private final RequestService requestService;

    public GroupMemberController(GroupMemberService memberService,
                                 RequestService requestService) {
        this.memberService =  memberService;
        this.requestService = requestService;
    }
    @PostMapping("/write")
    public String approveRequest(@RequestBody MyRequest id) {
        MembershipRequest membershipRequest = requestService.getAllRequestsById(id.getId());
        if (membershipRequest == null)
            return null;

        User user = membershipRequest.getUser();
        SocialGroup socialGroup = membershipRequest.getSocialGroup();
        Date ldt = new Date();

        GroupMember groupMember = new GroupMember();
        groupMember.setDateJoined(ldt);
        groupMember.setSocialGroup(socialGroup);
        groupMember.setUser(user);

        memberService.saveGroupMember(groupMember);

        requestService.deleteRequestById(id.getId());
        return "Vas zahtev za uclanjenje grupe je prihvacen!";
    }

}
class MyRequest{
    private Long id;
    public Long getId() {
        return id;
    }
}
