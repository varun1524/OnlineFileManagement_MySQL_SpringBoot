package com.server.dropbox_springboot_sever.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class UserProfile {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private Integer id;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String overview = "";
    private String mobile= "";
    private String work = "";
    private String lifeevent= "";
    private String educaiton= "";
    private boolean music= false;
    private boolean sports= false;
    private boolean reading= false;

    public String getEducaiton() {
        return educaiton;
    }

    public void setEducaiton(String educaiton) {
        this.educaiton = educaiton;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isSports() {
        return sports;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public boolean isReading() {
        return reading;
    }

    public void setReading(boolean reading) {
        this.reading = reading;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLifeevent() {
        return lifeevent;
    }

    public void setLifeevent(String lifeevent) {
        this.lifeevent = lifeevent;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}