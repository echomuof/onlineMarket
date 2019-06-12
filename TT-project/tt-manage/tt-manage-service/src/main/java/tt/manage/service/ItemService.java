package tt.manage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tt.common.bean.EasyUIResult;
import tt.common.service.ApiService;
import tt.manage.mapper.ItemMapper;
import tt.manage.pojo.Item;
import tt.manage.pojo.ItemDesc;
import tt.manage.pojo.ItemParamItem;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService extends BaseService<Item> {

    @Resource
    private ItemDescService itemDescService;
    @Resource
    private ItemParamItemService itemParamItemService;
    @Resource
    private ItemMapper itemMapper;
    @Resource
    private ApiService apiService;
    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Boolean saveItem(Item item, String desc,String itemParams) {
        //存商品
        item.setStatus(1);
        item.setId(null);
        Integer save1 = super.save(item);

        //存商品描述
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        Integer save2 = this.itemDescService.save(itemDesc);

        //存商品规格
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer save3 = this.itemParamItemService.save(itemParamItem);

        sendMsg(item.getId(), "insert");

        return save1.intValue() == 1 && save2.intValue() == 1 && save3.intValue() == 1;
    }

    public EasyUIResult queryItemList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Item.class);
        example.setOrderByClause("created DESC");
        List<Item> items = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<>(items);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public Boolean updateItem(Item item, String desc, String itemParams) {
        item.setStatus(null);
        Integer integer1 = super.updateSelective(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer integer2 = this.itemDescService.updateSelective(itemDesc);

        Integer integer3 = this.itemParamItemService.updateItemParam(itemParams,item.getId());


        sendMsg(item.getId(),"update");

        return integer1.intValue() == 1 && integer2.intValue() == 1;
    }

    private void sendMsg(Long itemId,String type) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("itemId", itemId);
            map.put("type", type);
            map.put("date", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item."+type, MAPPER.writeValueAsString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
