package com.example.gdtbg.buddy.buddy_01_join.buddy_01_join;

import android.app.Activity;
import android.util.Log;
import android.widget.CheckBox;

import com.example.gdtbg.buddy.R;

import java.util.ArrayList;

/**
 * Created by gdtbg on 2017-11-19.
 */

public class Join_CheckBox {

    Activity activity;

    CheckBox check_reading;
    CheckBox check_exciting;
    CheckBox check_restaurant;
    CheckBox check_sports;
    CheckBox check_movie;
    CheckBox check_music;
    CheckBox check_art;
    CheckBox check_game;
    CheckBox check_fasion;
    CheckBox check_news;
    CheckBox check_animal;
    CheckBox check_etc;

    CheckBox[] checkArray;


    public Join_CheckBox(Activity activity) {
        this.activity = activity;
        init();
    }

    public void init() {

        check_reading = (CheckBox) activity.findViewById(R.id.check_reading);
        check_exciting = (CheckBox) activity.findViewById(R.id.check_exciting);
        check_restaurant = (CheckBox) activity.findViewById(R.id.check_restaurant);
        check_sports = (CheckBox) activity.findViewById(R.id.check_sports);
        check_movie = (CheckBox) activity.findViewById(R.id.check_movie);
        check_music = (CheckBox) activity.findViewById(R.id.check_music);
        check_art = (CheckBox) activity.findViewById(R.id.check_art);
        check_game = (CheckBox) activity.findViewById(R.id.check_game);
        check_fasion = (CheckBox) activity.findViewById(R.id.check_fasion);
        check_news = (CheckBox) activity.findViewById(R.id.check_news);
        check_animal = (CheckBox) activity.findViewById(R.id.check_animal);
        check_etc = (CheckBox) activity.findViewById(R.id.check_etc);


        checkArray = new CheckBox[]{check_reading, check_exciting, check_restaurant, check_sports, check_movie, check_music,
                check_art, check_game, check_fasion, check_news, check_animal, check_etc};

    }

    public ArrayList<String> checking() {

        ArrayList<String> checkedList = new ArrayList<>();

        for (CheckBox box : checkArray) {

            if (box.isChecked()) {
                checkedList.add(box.getText().toString());
            }

        }

        return checkedList;


    }


    public void checkingArrayName(String interest) {

        switch (interest)
        {
            case "독서" : check_reading.setChecked(true); break;
            case "익사이팅" : check_exciting.setChecked(true); break;
            case "맛집" : check_restaurant.setChecked(true); break;
            case "스포츠" : check_sports.setChecked(true); break;
            case "영화" : check_movie.setChecked(true); break;
            case "음악감상" : check_music.setChecked(true); break;
            case "미술" : check_art.setChecked(true); break;
            case "게임" : check_game.setChecked(true); break;
            case "패션" : check_fasion.setChecked(true); break;
            case "기사" : check_news.setChecked(true); break;
            case "동물" : check_animal.setChecked(true); break;
            case "기타" : check_etc.setChecked(true); break;
        }
    }


}
