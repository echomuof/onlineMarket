package tt.manage.service;

import com.github.abel533.entity.Example;
import org.springframework.stereotype.Service;
import tt.manage.mapper.ItemParamItemMapper;
import tt.manage.pojo.ItemParamItem;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {

    @Resource
    ItemParamItemMapper itemParamItemMapper;

    public ItemParamItem queryByItemId(Long itemId) {
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(itemId);
        return super.queryOne(itemParamItem);
    }

    public Integer updateItemParam(String itemParams, Long itemId) {
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParams);
        itemParamItem.setUpdated(new Date());

        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        return this.itemParamItemMapper.updateByExampleSelective(itemParamItem, example);
    }
}
