package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Attending;

import java.util.List;

public interface AttendingService {
    void saveAttending(Attending attending);

    List<Attending> getAttendings(Long id);
}
