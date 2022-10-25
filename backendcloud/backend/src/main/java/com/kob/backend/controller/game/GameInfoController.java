package com.kob.backend.controller.game;

import com.kob.backend.pojo.GameInfo;
import com.kob.backend.service.game.GameInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game/")
public class GameInfoController {

    @Autowired
    GameInfoService gameInfoService;

    @GetMapping("/info/")
    public GameInfo info(Integer id) {
        return gameInfoService.getGameInfo(id);
    }

    @GetMapping("/infopage/")
    public List<GameInfo> page(Integer page, Integer size) {
        return gameInfoService.getGameInfoPage(page, size);
    }

    @GetMapping("/all/")
    public List<GameInfo> getAll() {
        return gameInfoService.getGameInfos();
    }
}
