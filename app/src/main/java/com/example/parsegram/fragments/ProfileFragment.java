package com.example.parsegram.fragments;

import android.util.Log;

import com.example.parsegram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "done: Issue with getting posts!", e);
                    return;
                }
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
                for(Post post: objects){
                    Log.i(TAG, "done: post: " + post.getDescription() + ", user: " + post.getUser().getUsername());
                }
            }
        });
    }
}
