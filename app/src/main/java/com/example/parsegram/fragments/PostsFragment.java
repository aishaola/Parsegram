package com.example.parsegram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parsegram.EndlessRecyclerViewScrollListener;
import com.example.parsegram.Likes;
import com.example.parsegram.Post;
import com.example.parsegram.PostsAdapter;
import com.example.parsegram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {
    protected RecyclerView rvPosts;
    protected EndlessRecyclerViewScrollListener scrollListener;
    public static final String TAG = PostsFragment.class.getSimpleName();
    List<Post> posts;
    PostsAdapter adapter;
    SwipeRefreshLayout swipeContainer;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        posts = new ArrayList<Post>();

        adapter = new PostsAdapter(getContext(), posts);
        queryPosts();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // *** Endless pagination feature implemented here: ***
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);
        // *** Endless pagination feature ends here: ***

        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.setAdapter(adapter);

        // *** Swipe to REFRESH feature implemented here: ***
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully
                queryPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // *** Swipe to REFRESH feature ends here: ***

    }

    protected void loadNextDataFromApi(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_CREATED_AT);
        query.setLimit(3);
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
                    queryLikes(post);
                    posts.add(post);
                    Log.i(TAG, "done: post: " + post.getDescription() + ", likes: " + post.usersLiked.size() +  ", user: " + post.getUser().getUsername());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void queryPosts(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_CREATED_AT);
        query.setLimit(3);
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
                scrollListener.resetState();
                for(Post post: objects){
                    //post.usersLiked.addAll(likeylikes);
                    queryLikes(post);
                    posts.add(post);
                    Log.i(TAG, "done: post: " + post.getDescription() + ", likes: " + post.usersLiked.size() +  ", user: " + post.getUser().getUsername());
                }

                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();
    }

    protected void queryLikes(final Post post) {
        ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);
        query.include(Likes.KEY_POST);
        query.include(Likes.KEY_USER);
        query.whereEqualTo(Likes.KEY_POST, post);

        query.findInBackground(new FindCallback<Likes>() {
            @Override
            public void done(List<Likes> objects, ParseException e) {
                List<ParseUser> usersLiked = new ArrayList<>();
                if(e != null){
                    Log.e(TAG, "done: Issue with getting posts!", e);
                    return;
                }
                // for each row in Likes.class, add User to corresponding Post's List of Users
                //int count = 0;
                for(Likes like: objects){
                    ParseUser userWhoLiked = like.getUser();
                    Log.i(TAG, "queryLikes: Just added user " +  userWhoLiked.getUsername() +
                            " to post with description: " + post.getDescription() + " " +
                            usersLiked.size());
                    post.addLike(userWhoLiked);
                }
            }
        });

    }
}