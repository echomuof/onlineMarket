package tt.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.swing.StringUIClientPropertyKey;
import tt.common.service.ApiService;
import tt.web.bean.Cart;
import tt.web.threadLocal.UserThreadLocal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    @Value("${CART_TT_URL}")
    private String CART_TT_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public List<Cart> queryCartList(){
        try {
            String jsonData = this.apiService.doGet(CART_TT_URL + "/service/api/cart/"+ UserThreadLocal.get().getId());
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
