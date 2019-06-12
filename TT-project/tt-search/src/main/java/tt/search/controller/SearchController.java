package tt.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import tt.search.bean.SearchResult;
import tt.search.service.ItemSearchService;

import java.io.UnsupportedEncodingException;

@Controller
public class SearchController {

    public static final Integer ROWS = 32;

    @Autowired
    private ItemSearchService itemSearchService;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(
            @RequestParam(value = "q") String keyWords,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            keyWords = new String(keyWords.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("query", keyWords);
        SearchResult searchResult = null;
        try {
            searchResult = itemSearchService.search(keyWords,page,ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            searchResult = new SearchResult(0L, null);
        }
        modelAndView.addObject("itemList", searchResult.getList());
        modelAndView.addObject("page", page);
        long total = searchResult.getTotal();
        int pages = (int) (total % ROWS == 0 ? total / ROWS : total / ROWS + 1);
        modelAndView.addObject("pages", pages);
        return modelAndView;
    }

}
