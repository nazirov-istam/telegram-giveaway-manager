package com.telegram_giveaway_manager.telegram_giveaway_manager.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Entity
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = ("bot_user"))
public class User {
    @Id
    @Column(name = ("chat_id"))
    Long chatId;
    @Basic
    @Column(name = ("first_name"))
    String firstName;
    @Basic
    @Column(name = ("last_name"))
    String lastName;
    @Basic
    @Column(name = ("phone"))
    String phone;
    @Basic
    @Column(name = ("instagram_id"))
    String instagramId;
    @Basic
    @Column(name = ("ticket_number"))
    private Long ticketNumber;
    @Basic
    @Column(name = ("success"))
    private String success;
}
