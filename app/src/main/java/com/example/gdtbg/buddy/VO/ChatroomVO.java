package com.example.gdtbg.buddy.VO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gdtbg on 2017-10-04.
 */

public class ChatroomVO implements Serializable {

    private String friend_uid;
    private String lastTime;
    private String lastContent;
    private ArrayList<String> chatUsers;
    private int notReadStack;

    public ChatroomVO() {
    }

    public String getFriend_uid() {
        return friend_uid;
    }

    public void setFriend_uid(String friend_uid) {
        this.friend_uid = friend_uid;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    public ArrayList<String> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<String> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public int getNotReadStack() {
        return notReadStack;
    }

    public void setNotReadStack(int notReadStack) {
        this.notReadStack = notReadStack;
    }
}
