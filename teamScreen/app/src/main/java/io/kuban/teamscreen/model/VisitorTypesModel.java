package io.kuban.teamscreen.model;

import java.util.List;

/**
 * Created by wangxuan on 17/11/9.
 */

public class VisitorTypesModel extends BaseModel {

    public String name;
    public boolean enabled;
    public boolean isSelected;
    public List<FieldsModel> fields;
    public NdaModel nda;
    public NdaModel badge;
    public NdaModel photo;
    public NdaModel plus_one;
//    public NdaModel final;
}
