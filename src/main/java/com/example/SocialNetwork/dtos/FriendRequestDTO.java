package com.example.SocialNetwork.dtos;

import com.example.SocialNetwork.entities.RequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {


    private Long id;

    private RequestStatus status = RequestStatus.PENDING;

    private Long id_user1;

    private Long id_user2;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date date;

    public RequestStatus isAccepted() {
        return status;
    }
}
