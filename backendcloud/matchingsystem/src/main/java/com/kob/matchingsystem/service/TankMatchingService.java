package com.kob.matchingsystem.service;

public interface TankMatchingService {
    String addPlayer(Integer userId, Integer rating);

    String removePlayer(Integer userId);
}
