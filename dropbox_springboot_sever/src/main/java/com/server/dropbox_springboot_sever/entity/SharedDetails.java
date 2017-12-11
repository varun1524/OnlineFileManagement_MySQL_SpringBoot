package com.server.dropbox_springboot_sever.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class SharedDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private Integer id;

    private Integer sharedItemId;

    private String sharedwith;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSharedwith() {
        return sharedwith;
    }

    public void setSharedwith(String sharedwith) {
        this.sharedwith = sharedwith;
    }

    public Integer getSharedItemId() {
        return sharedItemId;
    }

    public void setSharedItemId(Integer sharedItemId) {
        this.sharedItemId = sharedItemId;
    }
}