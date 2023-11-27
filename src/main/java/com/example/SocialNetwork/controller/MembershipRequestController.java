package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.service.RequestService;
import com.example.SocialNetwork.service.SocialGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/requests")
public class MembershipRequestController {
    private final RequestService requestService;
    private Long id;

    public MembershipRequestController(RequestService requestService){
        this.requestService = requestService;
    }

    @GetMapping("/all")
    public List<MembershipRequest> showAllRequests(){
        return requestService.getAllRequests();
    }

    @GetMapping("all/{id}")
    public MembershipRequest getRequestsById(@PathVariable Long id) {
        return requestService.getAllRequestsById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteRequestById(@PathVariable Long id) {
        requestService.deleteRequestById(id);

        return "Usesno ste obrisali grupu";
    }
}
