package com.example.parsegram;

import org.parceler.Parcel;

@Parcel
public class PostParcel {
    String username;
    String description;
    boolean userHasLiked;
    int likes;
    String imageUrl;
    String timestamp;

    public PostParcel(){

    }
    public PostParcel(Post post){
        username = post.getUser().getUsername();
        description = post.getDescription();
        userHasLiked = post.userHasLiked;
        likes = post.getNumberOfLikes();
        imageUrl = post.getImage().getUrl();
        timestamp = post.getRelativeTimeAgo();

    }

}
