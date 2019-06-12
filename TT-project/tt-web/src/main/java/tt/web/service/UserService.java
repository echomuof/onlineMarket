package tt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt.sso.query.api.UserQueryService;
import tt.sso.query.bean.User;

@Service
public class UserService {

    @Autowired
    private UserQueryService userQueryService;

    public User queryByToken(String token) {
        User user = this.userQueryService.queryByToken(token);
        return user;
    }
}
