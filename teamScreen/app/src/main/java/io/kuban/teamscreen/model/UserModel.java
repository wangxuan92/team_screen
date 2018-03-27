package io.kuban.teamscreen.model;

import java.util.Map;

/**
 * Created by wangxuan on 17/11/16.
 */

public class UserModel extends BaseModel {
    public UserModel(long dbId, Map<String, Object> userMap, String visitorsId, boolean isSignin) {
        this.dbId = dbId;
        this.userMap = userMap;
        this.visitorsId = visitorsId;
        this.isSignin = isSignin;
    }

    public UserModel() {
    }

    public Map<String, Object> userMap;
    public String visitorsId;
    public boolean isSignin;
    public long dbId;

    @Override
    public String toString() {
        return "UserModel{" +
                "userMap=" + userMap +
                ", visitorsId='" + visitorsId + '\'' +
                ", isSignin=" + isSignin +
                '}';
    }
}
