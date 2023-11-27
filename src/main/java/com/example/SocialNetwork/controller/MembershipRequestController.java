package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.service.MembershipRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class MembershipRequestController {
    private final MembershipRequestService requestService;
    private Long id;

    public MembershipRequestController(MembershipRequestService requestService){
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

        return "Usesno ste obrisali zahtev";
    }
}
