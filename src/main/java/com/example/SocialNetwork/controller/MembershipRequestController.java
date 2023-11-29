package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.MembershipRequestService;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membershiprequest")
public class MembershipRequestController {
    private final MembershipRequestService requestService;
    private final UserService userService;
    private final SocialGroupService groupService;
    private Long id;

    public MembershipRequestController(MembershipRequestService requestService, UserService userService, SocialGroupService groupService){
        this.requestService = requestService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping("/group/{id}")
    public List<MembershipRequest> showAllRequests(@PathVariable Long id){
        User user = userService.findCurrentUser();
        SocialGroup socialGroup = groupService.getSocialGroupById(id);
        if(socialGroup.getUser().getId()==user.getId()){
            return requestService.getAllRequests();
        }
        return  null;
    }

    @GetMapping("/{id}")
    public MembershipRequest getRequestsById(@PathVariable Long id) {
        return requestService.getAllRequestsById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteRequestById(@PathVariable Long id) {
        requestService.deleteRequestById(id);

        return "Usesno ste obrisali zahtev";
    }

    @GetMapping("/allrequestsforgroup/{id}")
    public List<MembershipRequest> showAllRequestsForSocialGroup(@PathVariable Long id){
        return requestService.getAllRequestsForSocialGroup(id);
    }
}
