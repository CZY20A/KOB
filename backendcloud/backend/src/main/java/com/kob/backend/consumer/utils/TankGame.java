package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.TankWebSocketServer;
import com.kob.backend.pojo.User;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TankGame extends Thread{
    private long lastTIme = System.currentTimeMillis();
    private long quietTime = 0;
    private Integer rows = 15;
    private Integer cols = 15;
    private Integer walls_cnt = 20;
    private Integer[][] g = new Integer[rows][cols];

    private Integer[] dir_x = {-1, 0, 1, 0};
    private Integer[] dir_y = {0, 1, 0, -1};

    //0123:上右下左 456:左中上发射子弹
    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private String loser = "none"; // A, B, all
    private String status;
    private Integer playerAId, playerBId;

    private Integer stopStepA = null;

    private Integer stopStepB = null;

    private ReentrantLock lock = new ReentrantLock();

    public Integer[][] getG() {
        return this.g;
    }

    public Integer getPlayerAId() {
        return playerAId;
    }

    public Integer getPlayerBId() {
        return playerBId;
    }

    public void setLoser(String loser) {
        lock.lock();
        try {
            this.loser = loser;
        } finally {
            lock.unlock();
        }
    }

    public void setStatus(String status) {
        lock.lock();
        try {
            this.status = status;
        } finally {
            lock.unlock();
        }
    }

    public TankGame(Integer playerAId, Integer playerBId) {
        createMap();
        this.playerAId = playerAId;
        this.playerBId = playerBId;
        this.status = "playing";
    }

    private boolean check_connectivity(int x, int y) {
        if(x == 1 && y == this.cols - 2) return true;
        g[x][y] = 1;
        int xx, yy;
        for(int i = 0; i < 4; ++i) {
            xx = x + this.dir_x[i];
            yy = y + this.dir_y[i];
            if(xx >= 0 && xx <= this.rows - 1 && yy >= 0 && yy <= this.cols - 1 && this.g[xx][yy] == 0) {
                if(check_connectivity(xx, yy)) {
                    g[x][y] = 0;
                    return true;
                }
            }
        }
        g[x][y] = 0;
        return false;
    }

    private boolean draw() {
        for(int i = 0; i < this.rows; ++i)
            for(int j = 0; j < this.cols; ++j)
                this.g[i][j] = 0;

        for(int i = 0; i < this.rows; ++i)
                this.g[i][0] = this.g[i][this.cols - 1] = g[0][i] = g[this.rows - 1][i] = 1;

        Random random = new Random();
        for(int i = 0; i < this.walls_cnt/ 2; ++i) {
            for (int j = 0; j < 6000; ++j) {
                int r = random.nextInt(this.rows), c = random.nextInt(this.cols);
                if (this.g[r][c] == 1) continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;
                boolean flag = false;
                for (int k = 0; k < 4; ++k) {
                    int rr = r + this.dir_x[k] * 2, cc = c + this.dir_y[k] * 2;
                    if (rr >= 0 && rr <= this.rows - 1 && cc >= 0 && cc <= this.cols - 1 && this.g[rr][cc] == 1) {
                        flag = true;
                        break;
                    }
                }
                if (flag) continue;
                if (this.g[r][c] == 0) {
                    this.g[r][c] = this.g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                    break;
                }
            }
        }

        return check_connectivity(this.rows - 2, 1);
    }

    private void createMap() {
        for(int i = 0; i < 6000; ++i)
            if(draw())
                break;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public void setStopStepA(Integer stopStepA) {
        lock.lock();
        try {
            this.stopStepA = stopStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setStopStepB(Integer stopStepB) {
        lock.lock();
        try {
            this.stopStepB = stopStepB;
        } finally {
          lock.unlock();
        }
    }
    private void sendAllMessage(String message) {
        if(TankWebSocketServer.users.get(playerAId) != null) {
            TankWebSocketServer.users.get(playerAId).sendMessage(message);
        }
        if(TankWebSocketServer.users.get(playerBId) != null) {
            TankWebSocketServer.users.get(playerBId).sendMessage(message);
        }
    }

    private void sendUnOperate() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event","unoperate");
            if(stopStepA != null)
                resp.put("a_operate", stopStepA);
            if(stopStepB != null)
                resp.put("b_operate", stopStepB);
            stopStepA = stopStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void sendOperate() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "operate");
            if(this.nextStepA != null)
                resp.put("a_operate", nextStepA);
            if(this.nextStepB != null)
                resp.put("b_operate", nextStepB);
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void updateUserRating(Integer userId, Integer rating) {
        User user = TankWebSocketServer.userMapper.selectById(userId);
        user.setRating(rating);
        TankWebSocketServer.userMapper.updateById(user);
    }

    private void saveToDatabase() {
        Integer ratingA = TankWebSocketServer.userMapper.selectById(playerAId).getRating();
        Integer ratingB = TankWebSocketServer.userMapper.selectById(playerBId).getRating();

        if("A".equals(loser)) {
            ratingA -= 2;
            ratingB += 5;
        } else if("B".equals(loser)) {
            ratingA += 5;
            ratingB -= 2;
        }

        if(!"all".equals(loser)) {
            updateUserRating(playerAId, ratingA);
            updateUserRating(playerBId, ratingB);
        }
    }

    public void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        while(true) {
            quietTime += System.currentTimeMillis() - lastTIme;
            lastTIme = System.currentTimeMillis();
            if(quietTime >= 1000 * 300) {
                System.out.println("长久未操作终止:"+quietTime);
                lock.lock();
                try {
                    loser = "all";
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
            if(!"playing".equals(this.status)) {
                sendResult();
                break;
            }
            if(this.nextStepA != null || this.nextStepB != null) {
                quietTime = 0;
                sendOperate();
            }
            if(this.stopStepA != null || this.stopStepB != null) {
                quietTime = 0;
                sendUnOperate();
            }
        }
    }
}
