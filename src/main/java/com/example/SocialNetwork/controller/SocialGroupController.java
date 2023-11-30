package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.helper.MyRequest;
import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.service.MembershipRequestService;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socialgroup")
public class SocialGroupController extends MyRequest {
    private final SocialGroupService groupService;
    private final UserService userService;
    private final MembershipRequestService requestService;

    public SocialGroupController(SocialGroupService groupService, UserService userService,
                                 MembershipRequestService requestService) {
        this.groupService = groupService;
        this.userService = userService;
        this.requestService = requestService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Levi9";
    }

    @PostMapping("/")
    public String createGroup(@RequestBody SocialGroup group) {
        User user = userService.findByID(1L);
        group.setUser(user);
        groupService.saveGroup(group);
        return "Usmesno sacuvana grupa";
    }

    @GetMapping("/all")
    public List<SocialGroupDTO> showAllSocialGroups(){
        return groupService.getAllSocialGroups();
    }

    @GetMapping("/name/{name}")
    public List<SocialGroupDTO> getSocialGroupByName(@PathVariable String name) {
        return groupService.getSocialGroupByName(name);
    }
    @GetMapping("/id/{id}")
    public SocialGroupDTO getSocialGroupDTOById(@PathVariable Long id) {
        return groupService.getSocialGroupDTOById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteSocialGroupById(@PathVariable Long id) {
        groupService.deleteSocialGroupById(id);

        return "Usesno ste obrisali grupu";
    }

    @PostMapping("/createmembershiprequest")
    public String createMembershipReques(@RequestBody MyRequest id) {
        SocialGroup socialGroup = groupService.getSocialGroupById(id.getId());

        User u = new User();
        u.setId(48L);

        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setSocialGroup(socialGroup);
        membershipRequest.setUser(u);
        membershipRequest.setRequestStatus(RequestStatus.PENDING);

        requestService.saveRequest(membershipRequest);

        return "Uspesno ste poslali request za uclanjnje u grupi";
    }
}
