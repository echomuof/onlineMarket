package tt.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tt.common.service.RedisService;
import tt.web.service.ItemService;

import javax.annotation.Resource;

@Controller
@RequestMapping("item/cache")
public class ItemCacheController {

    @Resource
    private RedisService redisService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Void> delCache(@PathVariable("itemId") Long itemId) {
        try {
            String key = ItemService.REDIS_KEY + itemId;
            this.redisService.delete(key);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
