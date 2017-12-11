package com.server.dropbox_springboot_sever.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class DropboxStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String type;

    private String path;

    private Date creationtime;

    private String size;

    private String ownerusername;

    private boolean starred = false;

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isSharedstatus() {
        return sharedstatus;
    }

    public void setSharedstatus(boolean sharedstatus) {
        this.sharedstatus = sharedstatus;
    }

    private boolean sharedstatus = false;

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(Date creationtime) {
        this.creationtime = creationtime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOwnerusername() {
        return ownerusername;
    }

    public void setOwnerusername(String ownerusername) {
        this.ownerusername = ownerusername;
    }
}
