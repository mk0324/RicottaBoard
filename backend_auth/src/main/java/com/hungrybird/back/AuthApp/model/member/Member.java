package com.hungrybird.back.AuthApp.model.member;


import com.hungrybird.back.AuthApp.model.User;
import com.hungrybird.back.AuthApp.model.audit.DateAudit;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "MEMBER")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member extends DateAudit {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(length = 36, name = "MEMBER_ROLE")
    private String memberRoleName;

    @Column(length = 36, name = "ATTENDANCE")
    private int attendance;

    @Column
    private String channelId;

    @Column
    private String email;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
