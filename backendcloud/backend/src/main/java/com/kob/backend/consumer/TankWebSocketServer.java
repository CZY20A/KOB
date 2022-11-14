package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.consumer.utils.TankGame;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import io.netty.util.internal.ConcurrentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/tank/{token}")  // 注意不要以'/'结尾
public class TankWebSocketServer {

    private Session session = null;

    private User user;

    final public static ConcurrentSet<Integer> matchingPool = new ConcurrentSet<>();

    final public static ConcurrentHashMap<Integer, TankWebSocketServer> users = new ConcurrentHashMap<>();

    public static UserMapper userMapper;

    private TankGame game = null;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        TankWebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        System.out.println("connect tank");
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);

        this.user = userMapper.selectById(userId);

        if(this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    public static void startGame(Integer aId, Integer bId) {
        User playerA = userMapper.selectById(aId), playerB = userMapper.selectById(bId);
        TankGame tankGame = new TankGame(aId, bId);

        JSONObject respGame = new JSONObject();
        respGame.put("map", tankGame.getG());
        respGame.put("a_id", aId);
        respGame.put("b_id", bId);

        if(playerA != null) {
            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", playerB.getUsername());
            respA.put("opponent_photo", playerB.getPhoto());
            respA.put("game", respGame);
            if(users.get(aId) != null) {
                users.get(aId).game = tankGame;
                users.get(aId).sendMessage(respA.toJSONString());
            }
        }
        if(playerB != null) {
            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", playerA.getUsername());
            respB.put("opponent_photo", playerA.getPhoto());
            respB.put("game", respGame);
            if(users.get(bId) != null) {
                users.get(bId).game = tankGame;
                users.get(bId).sendMessage(respB.toJSONString());
            }
        }

        tankGame.start();
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnect tank");
        if(this.user != null) {
            users.remove(user.getId());
        }
    }

    private void startMatching() {
        matchingPool.add(this.user.getId());
        while(matchingPool.size() >= 2) {
            Iterator<Integer> iterator = matchingPool.iterator();
            Integer aId = iterator.next();
            iterator.remove();
            Integer bId = iterator.next();
            iterator.remove();

            TankWebSocketServer.startGame(aId, bId);
        }
    }

    private void stopMatching() {
        matchingPool.remove(this.user.getId());
    }

    private void operate(Integer op) {
        if(Objects.equals(user.getId(), game.getPlayerAId())) {
            game.setNextStepA(op);
        }
        else if(Objects.equals(user.getId(), game.getPlayerBId())) {
            game.setNextStepB(op);
        }
    }

    private void unOperate(Integer op) {
        if(Objects.equals(user.getId(), game.getPlayerAId())) {
            game.setStopStepA(op);
        }
        else if(Objects.equals(user.getId(), game.getPlayerBId())) {
            game.setStopStepB(op);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("tank receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)) {
            System.out.println("tank start matching");
            startMatching();
        } else if("stop-matching".equals(event)) {
            System.out.println("tank stop matching");
            stopMatching();
        } else if("operate".equals(event)) {
            operate(data.getInteger("operate"));
        } else if("unoperate".equals(event)) {
            unOperate(data.getInteger("operate"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try{
                this.session.getBasicRemote().sendText(message);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
