package tt.manage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import tt.common.bean.EasyUIResult;
import tt.common.service.RedisService;
import tt.manage.mapper.ContentMapper;
import tt.manage.pojo.Content;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ContentService extends BaseService<Content> {

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String CONTENT_CATEGORY_ID = "CATEGORY_ID_";
    private static final Integer REDIS_TIME = 60 * 60 * 30 * 3;

    public EasyUIResult queryContentList(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
//        String key = null;
//        String str = null;
//        try {
//            key = CONTENT_CATEGORY_ID + categoryId.toString();
//            str = this.redisService.get(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<Content> contents = null;
//        Gson gson = new Gson();
//        if (StringUtils.isNotEmpty(str)) {
//            List<Content> contents2 = (Page) gson.fromJson(str, new TypeToken<List<Content>>() {
//            }.getType());
//            List<Content> contents1 = this.contentMapper.queryContentList(categoryId);
//            System.out.println("*");
//        } else {
            List<Content> contents = this.contentMapper.queryContentList(categoryId);
//            String value = gson.toJson(contents);
//            System.out.println(value);
//            try {
//                this.redisService.set(key, value,REDIS_TIME);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


        PageInfo<Content> contentPageInfo = new PageInfo<>(contents);
        return new EasyUIResult(contentPageInfo.getTotal(), contentPageInfo.getList());
    }

}
