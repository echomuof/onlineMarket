package tt.manage.mapper;

import com.github.abel533.mapper.Mapper;
import tt.manage.pojo.Content;

import java.util.List;


public interface ContentMapper extends Mapper<Content> {

    List<Content> queryContentList(Long categoryId);

}
