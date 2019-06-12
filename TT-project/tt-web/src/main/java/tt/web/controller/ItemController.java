package tt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tt.web.service.ItemService;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "item")
public class ItemController {

    @Resource
    private ItemService itemService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView item(@PathVariable("itemId") Long itemId) {
        ModelAndView modelAndView = new ModelAndView("item");
        modelAndView.addObject("item", itemService.queryByItemId(itemId));

        modelAndView.addObject("itemDesc", this.itemService.queryDescByItemId(itemId));

        modelAndView.addObject("itemParam", this.itemService.queryItemParamItemByItemId(itemId));
        return modelAndView;
    }
}
