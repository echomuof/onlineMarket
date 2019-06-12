package tt.sso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tt.common.service.RedisService;
import tt.sso.mapper.UserMapper;
import tt.sso.pojo.User;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer REDIS_TIME = 60 * 30;

    public Boolean check(String param, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(param);
                break;
            case 2:
                record.setPhone(param);
                break;
            case 3:
                record.setEmail(param);
                break;
            default:
                return Boolean.FALSE;
        }

        return this.userMapper.selectOne(record) == null;
    }

    public Boolean doRegister(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        return this.userMapper.insert(user) == 1;
    }

    public String doLogin(String username, String password) throws Exception {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null) {
            //用户名不存在
            return null;
        }
        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword())) {
            //密码不正确
            return null;
        }
        String token = "TOKEN_" + DigestUtils.md5Hex(username + System.currentTimeMillis());
        this.redisService.set(token, MAPPER.writeValueAsString(user), REDIS_TIME);
        return token;
    }

    public User queryUserByToken(String token) {
        String jsonData = this.redisService.get(token);
        if (StringUtils.isEmpty(jsonData)) {
            //登陆超时
            return null;
        }
        //重新设置redis中的生存时间
        this.redisService.expire(token, REDIS_TIME);
        try {
            return MAPPER.readValue(jsonData, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
