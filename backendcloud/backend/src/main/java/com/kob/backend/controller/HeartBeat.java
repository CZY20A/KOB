package com.kob.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeat {
    @GetMapping("/heartbeat/")
    public void hearBeat() {

    }
}
