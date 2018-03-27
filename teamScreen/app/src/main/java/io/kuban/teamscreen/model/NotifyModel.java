package io.kuban.teamscreen.model;

/**
 * Created by wangxuan on 17/11/9.
 */

public class NotifyModel extends BaseModel {

    /**
     * enable_host : true
     * enable_fallback : false
     * fallback_user : null
     * wechat : false
     * email : true
     * sms : false
     * dingding : false
     */

    public boolean enable_host;
    public boolean enable_fallback;
    public Object fallback_user;
    public boolean wechat;
    public boolean email;
    public boolean sms;
    public boolean dingding;

}
