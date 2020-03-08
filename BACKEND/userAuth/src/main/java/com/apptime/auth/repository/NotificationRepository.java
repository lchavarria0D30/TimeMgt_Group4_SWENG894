package com.apptime.auth.repository;

import com.apptime.auth.model.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author Qi Zhang
 * DAO for Notification
 * Use Case: TMGP4-40, TMGP4-38
 */
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("SELECT n FROM Notification n WHERE n.owner = ?1 AND n.delivered = false AND n.remindTime <= ?2")
    List<Notification> findNewNotifications(String owner, Date latestRemindTime, Sort sort);

    @Query("SELECT n FROM Notification n WHERE n.owner = ?1 AND n.type = ?2 AND n.key = ?3")
    List<Notification> findByOwnerTypeKey(String owner, String type, String key);
}
