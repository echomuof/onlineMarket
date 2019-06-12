package tt.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tt.sso.query.api.UserQueryService;
import tt.sso.query.bean.User;

@Service
public class UserService {

    @Value("${SSO_TT_URL}")
    public String SSO_TT_URL;

    @Autowired
    private UserQueryService userQueryService;

    public User queryByToken(String token) {
        return this.userQueryService.queryByToken(token);
    }
}
