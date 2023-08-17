package com.example.zpinowe2.Matches;

public class MatchesObject {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String desc;

    public MatchesObject (String userId, String name, String profileImageUrl, String desc){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.desc=desc;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
/*
public class MatchesObject {
    private String userId;
    public MatchesObject(String userId)
    {this.userId=userId;}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}*/