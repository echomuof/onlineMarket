package tt.manage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tt.common.bean.EasyUIResult;
import tt.manage.pojo.Content;
import tt.manage.service.ContentService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "content")
public class ContentController {

    @Resource
    private ContentService contentService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content) {
        try {
            content.setId(null);
            Integer save = this.contentService.save(content);
            if (save.intValue() == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping
    public ResponseEntity<EasyUIResult> queryContentList(
            @RequestParam(value = "categoryId") Long categoryId,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "rows") Integer rows
    ) {
        try {
            EasyUIResult easyUIResult = this.contentService.queryContentList(categoryId, page, rows);
            if (null == easyUIResult || easyUIResult.getRows().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}















