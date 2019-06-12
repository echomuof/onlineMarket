package tt.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tt.common.utils.CookieUtils;
import tt.sso.pojo.User;
import tt.sso.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    private static final String COOKIE_NAME = "TT_TOKEN";

    @RequestMapping(value = "register",method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(
            @PathVariable(value = "param") String param,
            @PathVariable(value = "type") Integer type
    ) {
        try {
            Boolean isCheck = this.userService.check(param, type);
            if (isCheck == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(!isCheck);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        if (bindingResult.hasErrors()) {//没有通过校验
            result.put("status", "400");
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            List<String> msgs = new ArrayList<>();
            for (ObjectError errorMsg : allErrors) {
                msgs.add(errorMsg.getDefaultMessage());
            }
            result.put("data", "参数有误！" + StringUtils.join(msgs, "|"));
            return result;
        }
        try {
            Boolean isRegister = this.userService.doRegister(user);
            if (isRegister) {
                result.put("status", "200");
            } else {
                result.put("status", "500");
                result.put("data", "哈哈哈哈哈");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "500");
            result.put("data", "哈哈哈哈哈");
        }
        return result;
    }

    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = null;
        try {
            String token = this.userService.doLogin(user.getUsername(), user.getPassword());
            result = new HashMap<>();
            if (StringUtils.isEmpty(token)) {
                result.put("status", "500");
                return result;
            }
            result.put("status", "200");
            CookieUtils.setCookie(request, response, COOKIE_NAME, token);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "500");
        }
        return result;
    }

    @RequestMapping(value = "{token}",method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


}
