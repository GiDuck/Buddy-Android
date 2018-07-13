package com.example.gdtbg.buddy.VO;

import java.io.Serializable;

/**
 * Created by gdtbg on 2017-11-04.
 */

public class ReplyVO implements Serializable{

    Long reply_time;
    String reply_writer_uid;
    String reply_content;

    public ReplyVO() {
    }

    public Long getReply_time() {
        return reply_time;
    }

    public void setReply_time(Long reply_time) {
        this.reply_time = reply_time;
    }

    public String getReply_writer_uid() {
        return reply_writer_uid;
    }

    public void setReply_writer_uid(String reply_writer_uid) {
        this.reply_writer_uid = reply_writer_uid;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }
}
