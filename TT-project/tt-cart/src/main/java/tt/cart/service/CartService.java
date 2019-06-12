package tt.cart.service;

import com.github.abel533.entity.Example;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tt.cart.bean.Item;
import tt.cart.mapper.CartMapper;
import tt.cart.pojo.Cart;
import tt.cart.threadLocal.UserThreadLocal;
import tt.sso.query.bean.User;

import java.util.Date;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId) {
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
        if (cart == null) {
            //不存在
            cart = new Cart();
            Item item = this.itemService.queryByItemId(itemId);
            cart.setItemId(itemId);
            cart.setUserId(user.getId());
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            if (null != item.getImage()) {
                cart.setItemImage(StringUtils.split(item.getImage(), ",")[0]);
            }
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            cart.setNum(1);
            this.cartMapper.insert(cart);
        } else {
            //存在
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }

    public List<Cart> queryCartList() {
        return this.queryCartList(UserThreadLocal.get().getId());
    }

    public void updateNum(Long itemId, Integer num) {
        Example example = new Example(Cart.class);
        Cart record = new Cart();
        record.setNum(num);
        example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", UserThreadLocal.get().getId());
        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void delete(Long itemId) {
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", UserThreadLocal.get().getId());
        this.cartMapper.deleteByExample(example);
    }

    public List<Cart> queryCartList(Long userId) {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created DESC");
        example.createCriteria().andEqualTo("userId", userId);
        List<Cart> cartList = this.cartMapper.selectByExample(example);
        return cartList;
    }
}
