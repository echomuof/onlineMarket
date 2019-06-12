package tt.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends tt.manage.pojo.Item {

    public String[] getImages() {
        return StringUtils.split(super.getImage(), ",");
    }
}
