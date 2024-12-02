package com.telegram_giveaway_manager.telegram_giveaway_manager.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = ("settings"))
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("id"), columnDefinition = "1")
    private Integer id;
    @Basic
    @Column(name = ("channel1_url"))
    private String channelUrl;
    @Basic
    @Column(name = ("channel1_id"))
    private String channelId;
    @Basic
    @Column(name = ("sendingId"))
    private String sendingId;
}
