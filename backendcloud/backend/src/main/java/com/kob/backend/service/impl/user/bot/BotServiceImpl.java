package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotServiceImpl implements BotService {

    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> add(Bot bot) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        bot.setUserId(user.getId());

        Map<String, String> map = new HashMap<>();

        String title = bot.getTitle();
        if(title == null || title.length() == 0){
            map.put("message", "标题不能为空");
            return map;
        }
        if(title.length() > 100){
            map.put("message", "标题长度不能大于100");
            return map;
        }

        String description = bot.getDescription();
        if(description == null || description.length() == 0){
            bot.setDescription("这个用户很懒,什么也没留下~");
        }
        if(description != null && description.length() > 300){
            map.put("message", "bot描述长度不能大于300");
            return map;
        }

        String content = bot.getContent();
        if(content == null || content.length() == 0){
            map.put("message", "代码不能为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("message", "代码长度不能超过10000");
            return map;
        }

        Date date = new Date();
        bot.setCreatetime(date);
        bot.setModifytime(date);
        botMapper.insert(bot);
        map.put("message", "success");

        return map;
    }

    @Override
    public Map<String, String> remove(Integer id) {

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int userId = user.getId();
        Bot bot  = botMapper.selectById(id);

        Map<String, String> map = new HashMap<>();

        if(bot == null){
            map.put("message", "Bot不存在或已被删除");
            return map;
        }
        if(bot.getUserId() != userId){
            map.put("message", "没有权限删除该map");
            return map;
        }

        botMapper.deleteById(id);
        map.put("message", "success");

        return map;
    }

    @Override
    public Map<String, String> update(Bot bot) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();

        String title = bot.getTitle();
        if(title == null || title.length() == 0){
            map.put("message", "标题不能为空");
            return map;
        }
        if(title.length() > 100){
            map.put("message", "标题长度不能大于100");
            return map;
        }

        String description = bot.getDescription();
        if(description == null || description.length() == 0){
            bot.setDescription("这个用户很懒,什么也没留下~");
        }
        if(description != null && description.length() > 300){
            map.put("message", "bot描述长度不能大于300");
            return map;
        }

        String content = bot.getContent();
        if(content == null || content.length() == 0){
            map.put("message", "代码不能为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("message", "代码长度不能超过10000");
            return map;
        }

        Bot currentBot = botMapper.selectById(bot.getId());
        if(currentBot == null){
            map.put("message", "bot不存在或已被删除");
            return map;
        }
        if(currentBot.getUserId() != user.getId()){
            map.put("message", "没有权限修改该bot");
            return map;
        }

        bot.setUserId(currentBot.getUserId());
        bot.setModifytime(new Date());
        bot.setCreatetime(currentBot.getCreatetime());
        botMapper.updateById(bot);
        map.put("message", "success");

        return map;
    }

    @Override
    public List<Bot> getList() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", user.getId());

        return botMapper.selectList(queryWrapper);
    }
}
