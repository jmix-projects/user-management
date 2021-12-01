package io.jmix.usermgmt.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "UMGMT_USER_SESSION")
@Entity(name = "umgmt_UserSession")
public class UserSession {
    @Column(name = "ID", nullable = false)
    @Id
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "LAST_REQUEST")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRequest;

    public Date getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Date lastRequest) {
        this.lastRequest = lastRequest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}