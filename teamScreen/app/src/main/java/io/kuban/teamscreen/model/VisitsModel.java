package io.kuban.teamscreen.model;


/**
 * Created by wangxuan on 17/11/8.
 */
public class VisitsModel extends BaseModel {
    /**
     * own_confirmed : own_unconfirmed
     * interviewee_confirmed : interviewee_confirmed
     * manager_confirmed : manager_confirmed
     * name : wangxuan
     * company : null
     * phone_num : 13718810290
     * expect_visit_at : 2019-04-27T13:00:00.000+08:00
     * arrived_at : 2017-11-08T15:52:37.296+08:00
     * visitable_type : Invitation
     * visitable_id : 15
     * email : null
     * location_id : 343
     * avatar : null
     * reason : 你猜
     * interviewee_name : 冯鑫禄12
     */

    public String own_confirmed;
    public String interviewee_confirmed;
    public String manager_confirmed;
    public String name;
    public String HeadPortraitPath;
    public String company;
    public String phone_num;
    public String expect_visit_at;
    public String arrived_at;
    public String visitable_type;
    public int visitable_id;
    public String email;
    public int location_id;
    public String avatar;
    public String reason;
    public String interviewee_name;
    public Object accept_invite_at;
    public Object approved_at;
    public Object approved_by;
    public String created_at;
    public CreatedModel created_by;
    public long expect_arrival_time;
    public Object extra_info;
    public int host_user_id;
    public Object notes;
    public String request_type;
    public String signed_in_at;
    public String signed_in_by;
    public Object signed_out_at;
    public Object signed_out_by;
    public String space_id;
    public String updated_at;
    public Object user_photo;
    public String visitor_type;
    public Object visitor_user_id;
    public String visits_code;
}
