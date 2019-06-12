package tt.sso.query.api;

import tt.sso.query.bean.User;


public interface UserQueryService {
    User queryByToken(String token);
}
