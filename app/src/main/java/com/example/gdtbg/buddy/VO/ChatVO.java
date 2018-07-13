package com.example.gdtbg.buddy.VO;

import java.io.Serializable;

/**
 * Created by gdtbg on 2017-10-04.
 */

public class ChatVO implements Serializable {

    private String chatroom_uid;
    private String user_uid;
    private String nickname;
    private Long time;
    private String content;

    public ChatVO() {
    }

    public String getChatroom_uid() {
        return chatroom_uid;
    }

    public void setChatroom_uid(String chatroom_uid) {
        this.chatroom_uid = chatroom_uid;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}