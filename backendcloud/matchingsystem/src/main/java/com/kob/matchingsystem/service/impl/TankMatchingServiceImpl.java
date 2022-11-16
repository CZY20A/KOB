package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.TankMatchingService;
import com.kob.matchingsystem.service.impl.utils.TankMatchingPool;
import org.springframework.stereotype.Service;

@Service
public class TankMatchingServiceImpl implements TankMatchingService {

    public final static TankMatchingPool tankMatchingPool = new TankMatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        tankMatchingPool.addPlayer(userId, rating);
        return "tank add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        tankMatchingPool.removePlayer(userId);
        return "tank remove player success";
    }
}
