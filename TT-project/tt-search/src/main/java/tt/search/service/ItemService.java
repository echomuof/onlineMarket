package tt.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tt.common.service.ApiService;
import tt.search.bean.Item;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class ItemService {

    @Value("${TT_MANAGE_URL}")
    private String TT_MANAGE_URL;

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryById(Long itemId) {
        String url = TT_MANAGE_URL + "/rest/api/item/" + itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNoneEmpty(jsonData)) {
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
