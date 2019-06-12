package tt.manage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tt.manage.pojo.ItemCat;
import tt.manage.service.ItemCatService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "item/cat")
public class ItemCatController {

    @Resource
    private ItemCatService itemCatService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCat(@RequestParam(value = "id",defaultValue = "0")Long parentId) {
        try {
            ItemCat record = new ItemCat();
            record.setParentId(parentId);
            List<ItemCat> itemCatList = this.itemCatService.queryListByWhere(record);
            if (itemCatList == null || itemCatList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
