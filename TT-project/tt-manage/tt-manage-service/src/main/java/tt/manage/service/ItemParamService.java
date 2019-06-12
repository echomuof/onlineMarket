package tt.manage.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import tt.common.bean.EasyUIResult;
import tt.manage.mapper.ItemParamMapper;
import tt.manage.pojo.ItemParam;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemParamService extends BaseService<ItemParam>{

    @Resource
    private ItemParamMapper itemParamMapper;

    public ItemParam queryByItemCatId(Long itemCatId) {
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(itemCatId);
        return super.queryOne(itemParam);
    }

    public EasyUIResult queryItemParamList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(ItemParam.class);
        example.setOrderByClause("created DESC");
        List<ItemParam> itemParams = this.itemParamMapper.selectByExample(example);
        PageInfo<ItemParam> pageInfo = new PageInfo<>(itemParams);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
