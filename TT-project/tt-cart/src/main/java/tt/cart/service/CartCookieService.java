package tt.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt.cart.bean.Item;
import tt.cart.pojo.Cart;
import tt.common.utils.CookieUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartCookieService {

    @Autowired
    private ItemService itemService;

    private static final String COOKIE_NAME = "TT_CART";

    private static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 12;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Cart> carts = this.queryCartList(request);

        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                break;
            }
        }
        if (cart == null) {
            Item item = this.itemService.queryByItemId(itemId);
            cart = new Cart();
            cart.setItemId(itemId);
            cart.setNum(1);
            cart.setItemTitle(item.getTitle());
            cart.setCreated(new Date());
            cart.setUpdated(item.getCreated());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImage().split(",")[0]);
            carts.add(cart);
        } else {
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
        }
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), COOKIE_TIME);
    }

    public List<Cart> queryCartList(HttpServletRequest request) throws IOException {
        String jsonData = CookieUtils.getCookieValue(request, COOKIE_NAME);
        List<Cart> carts = null;
        if (StringUtils.isEmpty(jsonData)) {
            return new ArrayList<Cart>();
        } else {
            carts = MAPPER.readValue(jsonData, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }
        return carts;
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                break;
            }
        }
        if (cart == null) {
            return;
        }
        cart.setNum(num);
        cart.setUpdated(new Date());
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), COOKIE_TIME);
    }

    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
            }
        }
        if (cart == null) {
            return;
        }
        carts.remove(cart);
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), COOKIE_TIME);
    }
}
