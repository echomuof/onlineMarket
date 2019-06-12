package tt.manage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tt.manage.pojo.ItemDesc;
import tt.manage.service.ItemDescService;

import javax.annotation.Resource;

@Controller
@RequestMapping("item/desc")
public class ItemDescController {

    @Resource
    private ItemDescService itemDescService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryItemDesc(@PathVariable("itemId") Long itemId) {
        try {
            ItemDesc record = new ItemDesc();
            record.setItemId(itemId);
            ItemDesc itemDesc = this.itemDescService.queryOne(record);
            if (itemDesc == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
