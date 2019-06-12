package tt.manage.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tt.common.bean.ItemCatResult;
import tt.manage.service.ItemCatService;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "api/item/cat")
public class ApiItemCatController {
    @Resource
    private ItemCatService itemCatService;

    /***
     * 对外提供接口，查询类目
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCatList() {
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            if (itemCatResult == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
