package tt.web.controller;

import com.sun.org.apache.bcel.internal.generic.MONITORENTER;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tt.web.bean.Cart;
import tt.web.bean.Item;
import tt.web.bean.Order;
import tt.sso.query.bean.User;
import tt.web.interceptor.UserLoginHandlerInterceptor;
import tt.web.service.CartService;
import tt.web.service.ItemService;
import tt.web.service.OrderService;
import tt.web.service.UserService;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "order")
public class OrderController {

    @Resource
    private ItemService itemService;

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ModelAndView order(@PathVariable("itemId")Long itemId) {
        ModelAndView modelAndView = new ModelAndView("order");
        Item item = this.itemService.queryByItemId(itemId);
        modelAndView.addObject("item", item);
        return modelAndView;
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submit(
            Order order,
            @CookieValue(value = UserLoginHandlerInterceptor.COOKIE_NAME) String token
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            User user = this.userService.queryByToken(token);
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
            String orderId = this.orderService.submit(order);
            if (StringUtils.isEmpty(orderId)) {
                map.put("status", 500);
            } else {
                map.put("status", 200);
                map.put("data", orderId);
            }
        } catch (Exception e) {
            map.put("status", 500);
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "success",method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id")String id) {
        ModelAndView modelAndView = new ModelAndView("success");
        Order order = this.orderService.queryOrderById(id);
        modelAndView.addObject("order", order);
        modelAndView.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return modelAndView;
    }

    @RequestMapping(value = "create",method = RequestMethod.GET)
    public ModelAndView toCartOrder() {
        ModelAndView modelAndView = new ModelAndView("order-cart");
        List<Cart> carts = this.cartService.queryCartList();
        modelAndView.addObject("carts", carts);
        return modelAndView;
    }
}
