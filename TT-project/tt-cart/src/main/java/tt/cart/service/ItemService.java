package tt.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tt.cart.bean.Item;
import tt.common.service.ApiService;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Value("${MANAGE_TT_URL}")
    private String TT_MANAGE_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryByItemId(Long itemId) {
        String url = TT_MANAGE_URL + "/rest/api/item/" + itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, Item.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
