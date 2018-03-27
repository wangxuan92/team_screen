package io.kuban.teamscreen.event;

import io.kuban.teamscreen.model.VisitsModel;
import retrofit2.Response;

/**
 * Created by wangxuan on 17/11/9.
 */

public class NetworkRequestStatusEvent {
    private boolean isAutomatic = false;
    private Response<VisitsModel> visitsModel;
    private Throwable throwable;
    private long dbID;

    public long getDbID() {
        return dbID;
    }

    public Response<VisitsModel> getVisitsModel() {
        return visitsModel;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean isAutomatic() {
        return isAutomatic;
    }


    public NetworkRequestStatusEvent(Response<VisitsModel> visitsModel, Throwable throwable, boolean isAutomatic) {
        this.visitsModel = visitsModel;
        this.isAutomatic = isAutomatic;
        this.throwable = throwable;
    }

    public NetworkRequestStatusEvent(Response<VisitsModel> visitsModel, Throwable throwable, boolean isAutomatic, long dbID) {
        this.visitsModel = visitsModel;
        this.isAutomatic = isAutomatic;
        this.throwable = throwable;
        this.dbID = dbID;
    }
}
