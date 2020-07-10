package com.example.parsegram;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Parcel(analyze={Post.class})
@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKES = "likes";
    public List<ParseUser> usersLiked;

    public Post(){
        super();
        usersLiked = new ArrayList<>();
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public String getPostCreatedAt() {
        Date createdAt = getDate("createdAt");
        if(createdAt!= null)
            return "200h";

        //String relativeDate = DateUtils.getRelativeTimeSpanString(createdAt.getTime(),
        //        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return "7h";
    }

    public String getRelativeTimeAgo(Date date) {
        String relativeDate = DateUtils.getRelativeTimeSpanString(date.getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }


    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public int getNumberOfLikes() { return getInt(KEY_LIKES); }

    public List<ParseUser> getUsersLiked() {
        return usersLiked;
    }

    public boolean userHasLikedPost(){
        for (ParseUser user: usersLiked) {
            if(user.getUsername().equals(ParseUser.getCurrentUser().getUsername()))
                return true;
        }
        return false;
    }

    public void saveLikes(){
        put(KEY_LIKES, usersLiked.size());
        saveInBackground();
    }

    public void addLike(ParseUser user) {
        usersLiked.add(user);
        saveLikes();
    }

    public void zeroLikes() {
        usersLiked = new ArrayList<>();
        saveLikes();
    }

    public void removeLike(ParseUser user) {
        usersLiked.remove(user);
        saveLikes();
    }
}
