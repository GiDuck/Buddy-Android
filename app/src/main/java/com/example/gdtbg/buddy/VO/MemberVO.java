package com.example.gdtbg.buddy.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gdtbg on 2017-10-04.
 */

public class MemberVO implements Serializable {

    private String email;
    private String uid;
    private String nickname;
    private Long joinDate;
    private String likeLang;
    private String useLang;
    private HashMap<String, FriendVO> friendList;
    private HashMap<String, ChatroomVO> chatroomList;
    private String profilePhoto;
    private Boolean identified;
    private String type;
    private ArrayList<String> interest;



    public MemberVO() {
    }

    public MemberVO(String email, String uid, String nickname, Long joinDate,
                    String likeLang, String useLang,
                    HashMap<String, FriendVO> friendList, HashMap<String, ChatroomVO> chatroomList,
                    String profilePhoto, Boolean identified, String type) {

        this.email = email;
        this.uid = uid;
        this.nickname = nickname;
        this.joinDate = joinDate;
        this.likeLang = likeLang;
        this.useLang = useLang;
        this.friendList = friendList;
        this.chatroomList = chatroomList;
        this.profilePhoto = profilePhoto;
        this.identified = identified;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Long joinDate) {
        this.joinDate = joinDate;
    }

    public String getLikeLang() {
        return likeLang;
    }

    public void setLikeLang(String likeLang) {
        this.likeLang = likeLang;
    }

    public String getUseLang() {
        return useLang;
    }

    public void setUseLang(String useLang) {
        this.useLang = useLang;
    }

    public HashMap<String, FriendVO> getFriendList() {
        return friendList;
    }

    public void setFriendList(HashMap<String, FriendVO> friendList) {
        this.friendList = friendList;
    }

    public HashMap<String, ChatroomVO> getChatroomList() {
        return chatroomList;
    }

    public void setChatroomList(HashMap<String, ChatroomVO> chatroomList) {
        this.chatroomList = chatroomList;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Boolean getIdentified() {
        return identified;
    }

    public void setIdentified(Boolean identified) {
        this.identified = identified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getInterest() {
        return interest;
    }

    public void setInterest(ArrayList<String> interest) {
        this.interest = interest;
    }
}
