package tt.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tt.common.service.ApiService;
import tt.common.service.RedisService;
import tt.manage.pojo.ItemDesc;
import tt.manage.pojo.ItemParamItem;
import tt.web.bean.Item;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class ItemService {

    @Resource
    private ApiService apiService;

    @Resource
    private RedisService redisService;

    @Value("${QUERY_ITEM_URL}")
    private String QUERY_ITEM_URL;

    @Value("${MANAGE_TT_URL}")
    private String TAOTAO_MANAGE_URL;

    @Value("${QUERY_ITEM_DESC_URL}")
    private String QUERY_ITEM_DESC_URL;

    @Value("${QUERY_ITEM_PARAM_URL}")
    private String QUERY_ITEM_PARAM_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL_";

    private static final Integer REDIS_TIME = 60 * 60 * 24;

    public Item queryByItemId(Long itemId) {
        String key = REDIS_KEY + itemId;
        try {
            String cacheData = this.redisService.get(key);
            if (StringUtils.isNotEmpty(cacheData)) {
                return MAPPER.readValue(cacheData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String jsonData = this.apiService.doGet(TAOTAO_MANAGE_URL + QUERY_ITEM_URL + itemId);

            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            try {
                this.redisService.set(key, jsonData, REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MAPPER.readValue(jsonData, Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryDescByItemId(Long itemId) {
        try {
            String jsonData = this.apiService.doGet(TAOTAO_MANAGE_URL + QUERY_ITEM_DESC_URL + itemId);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            ItemDesc itemDesc = MAPPER.readValue(jsonData, ItemDesc.class);
            return itemDesc;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryItemParamItemByItemId(Long itemId) {
        try {
            String s = this.apiService.doGet(TAOTAO_MANAGE_URL + QUERY_ITEM_PARAM_URL + itemId);
            if (StringUtils.isEmpty(s)) {
                return null;
            }
            ItemParamItem itemParamItem = MAPPER.readValue(s, ItemParamItem.class);
            String paramData = itemParamItem.getParamData();

            ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);

            StringBuilder sb = new StringBuilder();
            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");

            for (JsonNode param : arrayNode) {
                sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + param.get("group").asText()
                        + "</th></tr>");
                ArrayNode params = (ArrayNode) param.get("params");
                for (JsonNode p : params) {
                    sb.append("<tr><td class=\"tdTitle\">" + p.get("k").asText() + "</td><td>"
                            + p.get("v").asText() + "</td></tr>");
                }
            }

            sb.append("</tbody></table>");
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}