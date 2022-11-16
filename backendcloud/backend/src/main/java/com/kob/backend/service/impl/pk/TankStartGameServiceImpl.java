package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.TankWebSocketServer;
import com.kob.backend.service.pk.TankStartGameService;
import org.springframework.stereotype.Service;

@Service
public class TankStartGameServiceImpl implements TankStartGameService {
    @Override
    public String startGame(Integer aId, Integer bId) {
        TankWebSocketServer.startGame(aId, bId);
        return "start game success";
    }
}
