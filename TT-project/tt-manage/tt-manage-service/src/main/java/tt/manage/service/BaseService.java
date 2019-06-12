package tt.manage.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import tt.manage.pojo.BasePojo;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class BaseService<T extends BasePojo> {
    @Autowired
    private Mapper<T> mapper;

    /***
     * 根据主键id查找
     * @param id
     * @return
     */
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /***
     * 根据条件查找一条
     * @param record
     * @return
     */
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    /***
     * 查找全部
     * @return
     */
    public List<T> queryAll() {
        return this.mapper.select(null);
    }

    /***
     * 根据条件查找全部符合
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record) {
        return this.mapper.select(record);
    }

    /***
     * 根据条件查找并分页
     * @param record
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<T> list = this.mapper.select(record);
        return new PageInfo<T>(list);
    }

    /**
     * 插入
     * @param t
     * @return
     */
    public Integer save(T t) {
        t.setCreated(new Date());
        t.setUpdated(t.getCreated());
        return this.mapper.insert(t);
    }

    /***
     * 只插入非空字段
     * @param t
     * @return
     */
    public Integer saveSelective(T t) {
        t.setCreated(new Date());
        t.setUpdated(t.getCreated());
        return this.mapper.insertSelective(t);
    }

    /***
     * 更新
     * @param t
     * @return
     */
    public Integer update(T t) {
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(t);
    }

    /***
     * 更新非空字段
     * @param t
     * @return
     */
    public Integer updateSelective(T t) {
        t.setCreated(null);
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(t);
    }

    /***
     * 根据主键删除
     * @param id
     * @return
     */
    public Integer deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /***
     * 根据集合中的条件全部删除
     * @param list
     * @param clazz
     * @param property
     * @return
     */
    public Integer deleteByIds(List<Object> list, Class<T> clazz, String property) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, list);
        return this.mapper.deleteByExample(example);
    }

    /***
     * 根据条件删除
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record) {
        return this.mapper.delete(record);
    }
}
