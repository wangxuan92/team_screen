package io.kuban.teamscreen.model;

import java.util.List;

/**
 * Created by wangxuan on 17/11/9.
 */

public class FieldsModel extends BaseModel {

    /**
     * name : 姓名
     * required : true
     * modifable : false
     * deletable : false
     * field_type : text
     * options : []
     */

    public boolean custom;
    public String name;
    public String field_name;
    public boolean required;
    public boolean modifable;
    public boolean deletable;
    public String parameter_name;
    public String field_type;
    public List<String> options;
}
