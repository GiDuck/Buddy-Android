package com.example.gdtbg.buddy.VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by friedegg on 2017-10-02.
 */

public class MainboardVO implements Serializable {
    private String board_content;
    private String board_writer_uid;
    private HashMap<String,String> board_photos = new HashMap<>();
    private Long board_time;
    public Map<String, Boolean> likes = new HashMap<>();
    public int likeCount = 0;
    public Map<String, ReplyVO> replies = new HashMap<>();
    private ArrayList<String> interest;



    public MainboardVO() {

    }


    public String getBoard_content() {
        return board_content;
    }

    public void setBoard_content(String board_content) {
        this.board_content = board_content;
    }

    public String getBoard_writer_uid() {
        return board_writer_uid;
    }

    public void setBoard_writer_uid(String board_writer_uid) {
        this.board_writer_uid = board_writer_uid;
    }

    public HashMap<String, String> getBoard_photos() {
        return board_photos;
    }

    public void setBoard_photos(HashMap<String, String> board_photos) {
        this.board_photos = board_photos;
    }

    public Long getBoard_time() {
        return board_time;
    }

    public void setBoard_time(Long board_time) {
        this.board_time = board_time;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Map<String, ReplyVO> getReplies() {
        return replies;
    }

    public void setReplies(Map<String, ReplyVO> replies) {
        this.replies = replies;
    }

    public ArrayList<String> getInterest() {
        return interest;
    }

    public void setInterest(ArrayList<String> interest) {
        this.interest = interest;
    }
}



