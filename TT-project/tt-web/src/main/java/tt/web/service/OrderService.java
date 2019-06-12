package tt.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tt.common.service.ApiService;
import tt.common.service.httpclient.HttpResult;
import tt.web.bean.Order;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class OrderService {

    @Resource
    private ApiService apiService;

    @Value(value = "${ORDER_TT_URL}")
    private String ORDER_TT_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String submit(Order order) {
        String url = ORDER_TT_URL + "/order/create";
        try {
            HttpResult httpResult = this.apiService.doPost(url, MAPPER.writeValueAsString(order));
            if (httpResult.getStatusCode().intValue() == 200) {
                String body = httpResult.getData();
                JsonNode jsonNode = MAPPER.readTree(body);
                if (jsonNode.get("status").asInt() == 200) {
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order queryOrderById(String id) {
        String url = ORDER_TT_URL + "/order/query/" + id;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                Order order = MAPPER.readValue(jsonData, Order.class);
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
