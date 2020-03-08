package com.apptime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Qi Zhang
 * Persistent Object for Notification (and defining the data model)
 * Use Case: TMGP4-40, TMGP4-38
 */
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private int id;
    private String owner;
    private String type;
    private String key;
    private String content;
    private boolean delivered;
    private Date remindTime;

    public Notification() {

    }

    public Notification(String owner, String type, String key, String content, Date remindTime) {
        this.owner = owner;
        this.type = type;
        this.key = key;
        this.content = content;
        this.delivered = false;
        this.remindTime = remindTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    @Override
    public String toString() {
        return "Notifaction{" +
                "id=" + id +
                ", owner='" + owner + '\'' +
                ", type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", content='" + content + '\'' +
                ", delivered=" + delivered +
                ", remindTime=" + remindTime +
                '}';
    }
}
