package io.kuban.teamscreen.model;

import java.util.List;

/**
 * Created by wangxuan on 17/11/9.
 */

public class WelcomeModel extends BaseModel {


    /**
     * image_url : https://media-dev-ssl.kuban.io/static/ent-web/1512546883946good_star.png
     * slide_urls : []
     * hide_logo : true
     * logo : null
     * accent_color : null
     */

    public String image_url;
    public boolean hide_logo;
    public String logo;
    public String accent_color;
    public List<String> slide_urls;

}
