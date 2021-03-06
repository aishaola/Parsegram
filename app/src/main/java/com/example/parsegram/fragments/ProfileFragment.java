package com.example.parsegram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parsegram.LoginActivity;
import com.example.parsegram.MainActivity;
import com.example.parsegram.Post;
import com.example.parsegram.PostsAdapter;
import com.example.parsegram.R;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends PostsFragment {
    TextView tvUsername;
    Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        tvUsername = view.findViewById(R.id.tvUser);

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vieww) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "Couldn't logout", e);
                            return;
                        }

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        Log.i(TAG, "Logged out successfully");
                        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        getContext().startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(4);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Network error: Issue with getting posts!", e);
                    swipeContainer.setRefreshing(false);
                    return;
                }

                adapter.clear();

                for(Post post: objects){
                    post.initBoolAndLikes();
                    posts.add(post);
                    Log.i(TAG, "done: post: " + post.getDescription() + ", likes: " + post.likes +  ", user: " + post.getUser().getUsername());
                }

                adapter.addAll(posts);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void loadNextDataFromApi(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_CREATED_AT);
        query.setLimit(3);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        Post lastPost = posts.get(posts.size()-1);
        query.whereLessThan(Post.KEY_CREATED_AT, lastPost.getCreatedAt());

        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Network error: Issue with getting posts!", e);
                    swipeContainer.setRefreshing(false);
                    return;
                }

                for(Post post: objects){
                    //post.usersLiked.addAll(likeylikes);
                    post.initBoolAndLikes();
                    posts.add(post);
                    Log.i(TAG, "done: post: " + post.getDescription() + ", likes: " + post.likes +  ", user: " + post.getUser().getUsername());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


}
