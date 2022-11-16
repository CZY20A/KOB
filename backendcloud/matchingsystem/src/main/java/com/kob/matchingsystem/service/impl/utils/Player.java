package com.kob.matchingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer rating;
    private Integer waitingTime; // 等待时间
    private Integer botId;

    public Player(Integer id, Integer rating, Integer waitingTime) {
        this.id = id;
        this.rating = rating;
        this.waitingTime = waitingTime;
    }
}
