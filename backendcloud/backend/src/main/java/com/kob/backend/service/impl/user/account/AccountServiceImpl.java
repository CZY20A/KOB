package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.RegisterUser;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.AccountService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> info() {
        User user = JwtUtil.getUserByToken();

        Map<String, String> map = new HashMap<>();
        map.put("message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("rating", user.getRating().toString());

        return map;
    }

    @Override
    public Map<String, String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String, String> map = new HashMap<>();
        map.put("message", "success");
        map.put("token", jwt);

        return map;
    }

    @Override
    public Map<String, String> register(RegisterUser user) {
        Map<String, String> map = new HashMap<>();

        if(user.getUsername() == null){
            map.put("message", "?????????????????????");
            return map;
        }

        if(user.getPassword() == null || user.getConfirmedPassword() == null){
            map.put("message", "??????????????????");
            return map;
        }

        if(user.getPassword().length() == 0 || user.getConfirmedPassword().length() == 0){
            map.put("message", "??????????????????");
            return map;
        }

        user.setUsername(user.getUsername().trim());
        if(user.getUsername().length() == 0){
            map.put("message", "?????????????????????");
            return map;
        }

        if(user.getUsername().length() > 16){
            map.put("message", "???????????????????????????16");
            return map;
        }

        if(user.getPassword().length() > 100 || user.getConfirmedPassword().length() > 100){
            map.put("message", "????????????????????????100");
            return map;
        }

        if(!user.getPassword().equals(user.getConfirmedPassword())){
            map.put("message", "??????????????????????????????");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            map.put("message", "??????????????????");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String photo = "https://cdn.acwing.com/media/user/profile/photo/154818_lg_705200a344.jpg";
        User u = new User(null, user.getUsername(), encodedPassword, photo, 1500);
        userMapper.insert(u);
        map.put("message", "success");

        return map;
    }

    @Override
    public Map<String, String> changeUsername(String username, Integer id) {
        Map<String, String> map = new HashMap<>();
        username = username.trim();
        if("".equals(username) || username == null) {
            map.put("message", "?????????????????????");
            return map;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(userQueryWrapper);

        if(users != null && users.size() > 0) {
            map.put("message", "??????????????????");
            return map;
        }

        User user = userMapper.selectById(id);
        user.setUsername(username);
        userMapper.updateById(user);
        map.put("message", "success");
        return map;
    }

    @Override
    public Map<String, String> changeImage(String url, Integer id) {
        Map<String, String> map = new HashMap<>();
        if("".equals(url) || url == null) {
            map.put("message", "url????????????");
            return map;
        }
        if(url.lastIndexOf(".") == -1) {
            map.put("message", "url????????????");
            return map;
        }
        String exert = url.substring(url.lastIndexOf(".") + 1);
        if(!"jpeg".equals(exert) && !"png".equals(exert) && !"jpg".equals(exert)) {
            map.put("message", "?????????jpg???jpeg???png??????");
            return map;
        }
        User user = userMapper.selectById(id);
        user.setPhoto(url);
        userMapper.updateById(user);
        map.put("message", "success");
        return map;
    }
}
