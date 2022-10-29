package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final boolean[][] g;
    private final static int[] dirx = {-1, 0, 1, 0};
    private final static int[] diry = {0, 1, 0, -1};
    private boolean firstStep = true;

    private final Player playerA, playerB;

    private Integer nextStepA = null;

    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();

    private String status = "playing"; // playing -> finished
    private String loser = ""; // all:平局 A:A输 B:B输

    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";


    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB, Bot botA, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new boolean[rows][cols];
        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if(botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if(botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }
        this.playerA = new Player(idA, botIdA, botCodeA,rows - 2,  1, new ArrayList<>());
        this.playerB = new Player(idB, botIdB, botCodeB, 1, cols - 2, new ArrayList<>());
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

    public Player getPlayerA(){
        return playerA;
    }

    public Player getPlayerB(){
        return playerB;
    }

    public boolean[][] getG(){
        return g;
    }


    public boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;
        for(int i = 0; i < 4; ++i){
            int x = sx + dirx[i], y = sy + diry[i];
            if(x < 0 || x >= rows || y < 0 || y >= cols) continue;
            if(g[x][y] == false && check_connectivity(x, y, tx, ty) == true) {
                g[sx][sy] = false;
                return true;
            }
        }
        g[sx][sy] = false;
        return false;
    }


    private boolean draw() { //画地图

        for(int i = 0; i < rows; ++i)
            for(int j = 0; j < cols; ++j)
                g[i][j] = false;

        for(int i = 0; i < cols; ++i) g[0][i] = g[rows - 1][i] = true;
        for(int i = 0; i < rows; ++i) g[i][0] = g[i][cols - 1] = true;

        Random random = new Random();
        for(int i = 0; i < inner_walls_count / 2; ++i) {
            for(int j = 0; j < 1000; ++j){
                int r = random.nextInt(rows), c = random.nextInt(cols);
                if(r == 1 && c == cols - 2 || r == rows - 2 && c == 1 ) continue;
                if(g[r][c] == true || g[rows - 1 - r][cols - 1- c]) continue;
                g[r][c] = g[rows - 1 - r][cols - 1 - c] = true;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        //暴力出奇迹，直到两只蛇的起点都能连通为止
        for(int i = 0; i < 1000; ++i)
            if(draw() == true)
                break;
    }

    private String getInput(Player player) { // 将当前的局面信息编码成字符串
        // 信息格式: 地图信息#我的x坐标#我的y坐标#(我的操作)#对手的x坐标#对手的y坐标#(对手的操作)
        Player me, you;
        if(playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")";
    }

    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1))
            return; // 亲自出马,不需要执行代码
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user_id", player.getId().toString());
        map.add("bot_code", player.getBotCode().toString());
        map.add("input", getInput(player));
        WebSocketServer.restTemplate.postForObject(addBotUrl, map, String.class);
    }

    private boolean nextStep() { //等待两名玩家下一步操作
        //前端的移动速度是1s走5个格子，为确保不会因输入过快导致漏掉操作而需要进行等待
        int t = 500;
        if(firstStep) {
             t = 2000; // 第一步要等2s种再接受输入这和前端等待两秒再进入游戏界面相符
        }
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        int n = 50;
        if(firstStep){
            n = 120;
            firstStep = false;
        }

        for(int i = 0; i < n; ++i){
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void sendAllMessage(String message) {
        if(WebSocketServer.users.get(playerA.getId()) != null)
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId()) != null)
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private  void sendResult() { //向两个client公布结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    private boolean checkValid(List<Cell> cellA, List<Cell> cellB) {
        int n = cellA.size();
        Cell cell = cellA.get(n - 1);
        if(g[cell.getX()][cell.getY()]) return false;

        for(int i = 0; i < n - 1; ++i){
            if(cellA.get(i).getX() == cell.getX() && cellA.get(i).getY() == cell.getY())
                return false;
        }

        for(int i = 0; i < n - 1; ++i) {
            if(cellB.get(i).getX() == cell.getX() && cellB.get(i).getY() == cell.getY())
                return false;
        }

        return true;
    }

    private void judge() { //判断两名玩家下一步操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean vailA = checkValid(cellsA, cellsB), vailB = checkValid(cellsB, cellsA);

        if(!vailA || !vailB) {
            status = "finished";

            if(!vailA && !vailB) loser = "all";
            else if(!vailB) loser = "B";
            else if(!vailA) loser = "A";
        }
    }


    private void sendMove() { //向两个client传递移动信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; ++i) {
            for(int j = 0; j < cols; ++j){
                res.append(g[i][j] == true? 1 : 0);
            }
        }
        return res.toString();
    }

    private void saveToDatabase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                loser,
                new Date(),
                getMapString()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    @Override
    public void run() {
        //操作肯定不会超过一千步
        for(int i = 0; i < 1000; ++i){
            if(nextStep()){ //是否获取两条蛇下一步操作
                judge();
                if("playing".equals(status)){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            }else{
                status = "finished";
                lock.lock(); //防止边界时间时读取操作
                try{
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    }else if(nextStepA == null){
                        loser = "A";
                    }else{
                        loser = "B";
                    }
                }finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
