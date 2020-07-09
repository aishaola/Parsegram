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
    public static final String KEY_CREATED = "createdAt";
    public static final String KEY_LIKES = "likes";
    List<ParseUser> usersLiked;

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

    public void addLike(ParseUser user) {
        usersLiked.add(user);
        put(KEY_LIKES, (getLikes() + 1));
        saveInBackground();
    }

    public void zeroLikes() {
        usersLiked = new ArrayList<>();
        put(KEY_LIKES, 0);
        saveInBackground();
    }

    public void removeLike(ParseUser user) {
        usersLiked.remove(user);
        put(KEY_LIKES, (getLikes() - 1));
        saveInBackground();
    }
}
