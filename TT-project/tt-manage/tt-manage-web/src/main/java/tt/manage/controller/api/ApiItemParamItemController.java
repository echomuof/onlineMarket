package tt.manage.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tt.manage.pojo.ItemParamItem;
import tt.manage.service.ItemParamItemService;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "api/item/param/item")
public class ApiItemParamItemController {

    @Resource
    private ItemParamItemService itemParamItemService;
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryItemParamItemByItemId(@PathVariable("itemId")Long itemId) {
        try {
            ItemParamItem itemParamItem = this.itemParamItemService.queryByItemId(itemId);
            if (itemParamItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
