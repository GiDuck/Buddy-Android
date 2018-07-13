package com.example.gdtbg.buddy.VO;

import java.io.Serializable;

/**
 * Created by gdtbg on 2017-10-09.
 */

public class FriendVO implements Serializable {

    private String friend_uid;
    private Boolean isVerified;

    public FriendVO() {
        isVerified = false;


    }

    public FriendVO(String friend_uid) {
        this.friend_uid = friend_uid;
        isVerified = false;
    }

    public String getFriend_uid() {
        return friend_uid;
    }

    public void setFriend_uid(String friend_uid) {
        this.friend_uid = friend_uid;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
