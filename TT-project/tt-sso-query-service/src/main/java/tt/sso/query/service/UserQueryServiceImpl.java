package tt.sso.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt.common.service.RedisService;
import tt.sso.query.api.UserQueryService;
import tt.sso.query.bean.User;

import java.io.IOException;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer REDIS_TIME = 60 * 30;

    @Override
    public User queryByToken(String token) {
        String key = token;
        String jsonData = this.redisService.get(key);
        if (StringUtils.isEmpty(jsonData)) {
            return null;
        }
        this.redisService.expire(key, REDIS_TIME);
        try {
            return MAPPER.readValue(jsonData, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
