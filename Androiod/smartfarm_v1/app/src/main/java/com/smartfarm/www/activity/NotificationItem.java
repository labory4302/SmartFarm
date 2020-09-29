package com.smartfarm.www.activity;

public class NotificationItem {

    String notificationTitle;
    String notificationContents;

    public NotificationItem(String notificationTitle, String notificationContents) {
        this.notificationTitle = notificationTitle;
        this.notificationContents = notificationContents;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationContents() {
        return notificationContents;
    }

    public void setNotificationContents(String notificationContents) {
        this.notificationContents = notificationContents;
    }
}
