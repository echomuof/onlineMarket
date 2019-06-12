package tt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tt.web.service.IndexService;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "index")
public class IndexController {

    @Resource
    private IndexService indexService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toIndex() {
        ModelAndView modelAndView = new ModelAndView("index");
        //首页大广告
        String indexAD1 = this.indexService.queryIndexAD1();
        modelAndView.addObject("indexAD1", indexAD1);

        //首页小广告
        String indexAD2 = this.indexService.queryIndexAD2();
        modelAndView.addObject("indexAD2", indexAD2);

        return modelAndView;
    }
}
