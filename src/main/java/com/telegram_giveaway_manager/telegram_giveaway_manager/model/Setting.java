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
    @Column(name = "id", columnDefinition = "1")
    private Integer id;
    @Basic
    @Column(name = ("channel1_url"))
    private String channel1Url;
    @Basic
    @Column(name = ("channel2_url"))
    private String channel2Url;
    @Basic
    @Column(name = ("channel1_id"))
    private String channel1Id;
    @Basic
    @Column(name = ("channel2_id"))
    private String channel2Id;
    @Basic
    @Column(name = ("sendingId"))
    private String sendingId;
}
