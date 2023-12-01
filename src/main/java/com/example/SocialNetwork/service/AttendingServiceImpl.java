package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Attending;
import com.example.SocialNetwork.repository.AttendingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AttendingServiceImpl implements AttendingService{

    private AttendingRepository attendingRepository;

    public AttendingServiceImpl(AttendingRepository attendingRepository) {
        this.attendingRepository = attendingRepository;
    }

    @Override
    public void saveAttending(Attending attending) {
        attendingRepository.save(attending);
    }

    @Override
    public List<Attending> getAttendings(Long id) {
        return this.attendingRepository.findAllByEventId(id);
    }
}