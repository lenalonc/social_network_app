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
            requestService.getAllRequestsForSocialGroup(id);
        }
        return  null;
    }
    @GetMapping("/{id}")
    public MembershipRequest getRequestsById(@PathVariable Long id) {
        User currentUser = userService.findCurrentUser();
        MembershipRequest request = requestService.getAllRequestsById(id);
        if (request.getUser().getId() == currentUser.getId() || request.getSocialGroup().getUser().getId()==currentUser.getId())
            return request;
        else
            return null;
    }

    @DeleteMapping("/{id}")
    public String deleteRequestById(@PathVariable Long id) {
        User currentUser = userService.findCurrentUser();
        MembershipRequest request = requestService.getAllRequestsById(id);
        if (request.getUser().getId() == currentUser.getId() || request.getSocialGroup().getUser().getId()==currentUser.getId()){

            requestService.deleteRequestById(id);
            return "Uspesno ste obrisali zahtev";
        }
        else
            return "Nemate pravo na brisanje zahteva";
    }
}
