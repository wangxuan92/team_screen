package io.kuban.teamscreen.event;

/**
 * Created by wangxuan on 17/11/8.
 */

public class ReturnHomePageEvent {
    private boolean isReturnHomePage;

    public boolean isReturnHomePage() {
        return isReturnHomePage;
    }

    public void setReturnHomePage(boolean returnHomePage) {
        isReturnHomePage = returnHomePage;
    }

    public ReturnHomePageEvent(boolean isReturnHomePage) {
        this.isReturnHomePage = isReturnHomePage;
    }
}
