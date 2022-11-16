package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TankMatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();

    private final static String startGameUrl = "http://127.0.0.1:3000/pk/tank/start/game/";
    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        TankMatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for(Player player : players) {
                if(player.getId() != userId)
                    newPlayers.add(player);
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {
        for(Player player : players)
            player.setWaitingTime(player.getWaitingTime() + 1);
    }

    private void sendResult(Player a, Player b) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getId().toString());
        data.add("b_id", b.getId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    private boolean checkMatched(Player a, Player b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void matchPlayers() {
        boolean[] used = new boolean[players.size()];
        for(int i = 0; i < players.size(); ++i){
            if(used[i]) continue;
            for(int j = i + 1; j < players.size(); ++j) {
                if(used[j]) continue;
                if(checkMatched(players.get(i), players.get(j))) {
                    used[i] = used[j] = true;
                    sendResult(players.get(i), players.get(j));
                    break;
                }
            }
        }

        List<Player> newPlayers = new ArrayList<>();
        for(int i = 0; i < players.size(); ++i)
            if(!used[i])
                newPlayers.add(players.get(i));
        players = newPlayers;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try{
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
