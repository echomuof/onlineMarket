package tt.manage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tt.manage.pojo.ContentCategory;
import tt.manage.service.ContentCategoryService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "content/category")
public class ContentCategoryController {

    @Resource
    private ContentCategoryService contentCategoryService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryListByParentId(
            @RequestParam(value = "id", defaultValue = "0")Long parentId
    ) {
        try {
            List<ContentCategory> contentCategoryList = this.contentCategoryService.queryListByParentId(parentId);
            if (contentCategoryList == null || contentCategoryList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(contentCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContentCategory(ContentCategory contentCategory) {
        try {
            this.contentCategoryService.saveContentCategory(contentCategory);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> update(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "name") String name
    ) {
        try {
            Boolean isUpdate = this.contentCategoryService.updateName(id, name);
            if (isUpdate) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContentCategory(
            @RequestParam(value = "parentId") Long parentId,
            @RequestParam(value = "id") Long id
    ) {
        try {
            this.contentCategoryService.deleteContentCategory(parentId, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}