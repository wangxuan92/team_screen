package io.kuban.teamscreen.event;

/**
 * Created by wangxuan on 17/6/29.
 */

public class CameraPreviewRefreshEvent {
    private String file;

    public String getFile() {
        return file;
    }

    public CameraPreviewRefreshEvent(String file) {
        this.file = file;
    }
}
