package com.example.parsegram;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

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

    public int getLikes() { return getInt(KEY_LIKES); }

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

    public void addLike(ParseUser user) {
        usersLiked.add(user);
        put(KEY_LIKES, usersLiked.size());
        saveInBackground();
    }

    public void zeroLikes() {
        usersLiked = new ArrayList<>();
        put(KEY_LIKES, 0);
        saveInBackground();
    }

    public void removeLike(ParseUser user) {
        usersLiked.remove(user);
        put(KEY_LIKES, usersLiked.size());
        saveInBackground();
    }
}
