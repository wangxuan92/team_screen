package io.kuban.teamscreen.model;

import java.util.List;

/**
 * Created by wangxuan on 18/3/28.
 */

public class AreasModel {


    /**
     * id : 52
     * name : 北2区
     * map_id : null
     * area_type : public_office
     * image : null
     * organizations : [{"id":14,"name":"测功","full_name":"测试公司","logo":null,"space_id":364,"status":"active","is_entered":true,"is_archived":false,"name_pinyin":"ce gong"},{"id":6,"name":"小豆科技","full_name":"小豆科技","logo":null,"space_id":364,"status":"active","is_entered":true,"is_archived":false,"name_pinyin":"xiao dou ke ji"}]
     */

    public int id;
    public String name;
    public Object map_id;
    public List<String> lock_qrcode_urls;
    public String area_type;
    public Object image;
    public List<OrganizationsModel> organizations;

}
