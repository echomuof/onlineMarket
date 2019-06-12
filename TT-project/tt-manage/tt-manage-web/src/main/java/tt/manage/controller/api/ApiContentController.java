package tt.manage.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tt.common.bean.EasyUIResult;
import tt.manage.service.ContentService;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "api/content")
public class ApiContentController {

    @Resource
    private ContentService contentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryContentList(
            @RequestParam(value = "categoryId") Long categoryId,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "rows") Integer rows
    ) {
        try {
            //TODO
            EasyUIResult easyUIResult = this.contentService.queryContentList(categoryId, page, rows);
            if (easyUIResult == null || easyUIResult.getRows().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
