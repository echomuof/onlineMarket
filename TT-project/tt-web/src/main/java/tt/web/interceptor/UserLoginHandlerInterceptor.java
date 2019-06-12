package tt.web.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tt.common.utils.CookieUtils;
import tt.sso.query.bean.User;
import tt.web.service.UserService;
import tt.web.threadLocal.UserThreadLocal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Resource
    private UserService userService;

    @Value("${SSO_TT_URL}")
    private static String SSO_TT_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        String url = SSO_TT_URL + "/user/login.html";
        if (StringUtils.isEmpty(token)) {
            response.sendRedirect(url);
            return false;
        }
        User user = this.userService.queryByToken(token);
        if (user == null) {
            response.sendRedirect(url);
            return false;
        }
        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.set(null);
    }
}
