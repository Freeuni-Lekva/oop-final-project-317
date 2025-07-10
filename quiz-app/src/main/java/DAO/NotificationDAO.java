package DAO;

import models.Notification;

import java.util.ArrayList;

public interface NotificationDAO {
    public void sendNotification(Notification notification);

    public void deleteNotification(Notification notification);

    public void deleteNotification(long notificationId);

    public ArrayList<Notification> getNotifications(long userId);
}
