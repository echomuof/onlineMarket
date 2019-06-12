package tt.manage.service;

import org.springframework.stereotype.Service;
import tt.manage.pojo.Content;
import tt.manage.pojo.ContentCategory;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryService extends BaseService<ContentCategory>{
    public List<ContentCategory> queryListByParentId(Long i) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(i);
        return super.queryListByWhere(contentCategory);
    }

    public void saveContentCategory(ContentCategory contentCategory) {
        contentCategory.setId(null);
        contentCategory.setIsParent(false);
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        super.save(contentCategory);

        ContentCategory parent = super.queryById(contentCategory.getParentId());
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            super.update(parent);
        }
    }

    public Boolean updateName(Long id, String name) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setId(id);
        contentCategory.setName(name);
        return super.updateSelective(contentCategory).intValue() == 1;
    }

    public void deleteContentCategory(Long parentId, Long id) {
        //查找id节点下的所有节点
        List<Object> ids = new ArrayList<>();
        ids.add(id);
        findAllSubNode(id, ids);
        super.deleteByIds(ids, ContentCategory.class, "id");

        ContentCategory record = new ContentCategory();
        record.setParentId(parentId);
        List<ContentCategory> contentCategoryList = super.queryListByWhere(record);
        if (contentCategoryList == null || contentCategoryList.isEmpty()) {
            ContentCategory parent = new ContentCategory();
            parent.setId(parentId);
            parent.setIsParent(false);
            super.updateSelective(parent);
        }
    }

    private void findAllSubNode(Long pid, List<Object> ids) {
        ContentCategory record = new ContentCategory();
        record.setParentId(pid);
        List<ContentCategory> contentCategoryList = super.queryListByWhere(record);
        for (ContentCategory contentCategory : contentCategoryList) {
            ids.add(contentCategory.getId());
            if (contentCategory.getIsParent() == true) {
                findAllSubNode(contentCategory.getId(), ids);
            }
        }
    }


}
