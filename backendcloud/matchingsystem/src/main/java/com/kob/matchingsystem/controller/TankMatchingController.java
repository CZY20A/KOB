package com.kob.matchingsystem.controller;

import com.kob.matchingsystem.service.TankMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/tank/player/")
public class TankMatchingController {
    @Autowired
    TankMatchingService tankMatchingService;

    @PostMapping("/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return tankMatchingService.addPlayer(userId, rating);
    }

    @PostMapping("/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return tankMatchingService.removePlayer(userId);
    }
}
