package io.kuban.teamscreen.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * Created by wangxuan on 17/11/18.
 */
@Entity
public class UserDB {
    @Id
    private Long id;
    public String userMap;
    public String visitorsId;
    public boolean isSignin;

    public boolean getIsSignin() {
        return this.isSignin;
    }

    public void setIsSignin(boolean isSignin) {
        this.isSignin = isSignin;
    }

    public String getVisitorsId() {
        return this.visitorsId;
    }

    public void setVisitorsId(String visitorsId) {
        this.visitorsId = visitorsId;
    }

    public String getUserMap() {
        return this.userMap;
    }

    public void setUserMap(String userMap) {
        this.userMap = userMap;
    }

    @Generated(hash = 1516774608)
    public UserDB(Long id, String userMap, String visitorsId, boolean isSignin) {
        this.id = id;
        this.userMap = userMap;
        this.visitorsId = visitorsId;
        this.isSignin = isSignin;
    }

    public UserDB(String userMap, String visitorsId, boolean isSignin) {
        this.userMap = userMap;
        this.visitorsId = visitorsId;
        this.isSignin = isSignin;
    }

    @Generated(hash = 1312299826)
    public UserDB() {
    }

    @Override
    public String toString() {
        return "UserDB{" +
                "userMap='" + userMap + '\'' +
                ", visitorsId='" + visitorsId + '\'' +
                ", isSignin=" + isSignin +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
