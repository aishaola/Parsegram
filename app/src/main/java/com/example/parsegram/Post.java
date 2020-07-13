package com.example.parsegram;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    public static final String KEY_USERS_LIKED = "usersLiked";
    public List<ParseUser> usersLiked;
    public int likes;
    public boolean userHasLiked;

    public Post(){
        super();
        likes = 0;
        userHasLiked = false;
        usersLiked = new ArrayList<>();
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
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

    public int getLikes() {
        return likes;
    }

    // Sets a boolean variable field to indicate whether current user has liked given post
    public void initBoolAndLikes(){
        JSONArray jsonArray = getJSONArray(KEY_USERS_LIKED);
        if(jsonArray == null){
            setUserLiked(false);
            return;
        }

        likes = jsonArray.length();
        String userId = ParseUser.getCurrentUser().getObjectId();

        //Checks if user is in userhasliked array for the post
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (jsonArray.getJSONObject(i).getString("objectId").equals(userId)){
                    setUserLiked(true);
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setUserLiked(false);
    }

    public void setUserLiked(boolean val) {
        userHasLiked = val;
    }

    public void addUserLiked() {
        likes++;
        setUserLiked(true);
        add(KEY_USERS_LIKED, ParseUser.getCurrentUser());
        saveInBackground();
    }

    public void removeUserLiked() {
        likes--;
        setUserLiked(false);
        removeAll(KEY_USERS_LIKED, Collections.singleton(ParseUser.getCurrentUser()));
        saveInBackground();
    }

    public void updateLike(){
        if(userHasLiked)
            removeUserLiked();
        else
            addUserLiked();
    }

    public String getRelativeTimeAgo() {
        String rawJsonDate = getCreatedAt().toString();
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


}
