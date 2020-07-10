package com.example.parsegram;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    // the movie to display
    PostParcel post;

    // the view objects
    ImageView ivImage;
    ImageView ivLike;
    TextView tvUsername;
    TextView tvUsername2;
    TextView tvDescription;
    TextView tvLikes;
    TextView tvWordLike;
    TextView tvTimestamp;
    int likes;
    boolean userHasLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post);
        // resolve the view objects
        ivImage = findViewById(R.id.ivImage);
        ivLike = findViewById(R.id.ivLikeBtn);
        tvDescription = findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        tvLikes = findViewById(R.id.tvLikes);
        tvWordLike = findViewById(R.id.tvWordLikes);
        tvTimestamp = findViewById(R.id.tvTS);

        // unwrap the movie passed in via intent, using its simple name as a key
        post = (PostParcel) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostDetailsActivity", String.format("Showing details for '%s'", post.description));

        tvDescription.setText(post.description);
        tvUsername.setText(post.username);
        tvUsername2.setText(post.username);
        tvTimestamp.setText(post.timestamp);
        userHasLiked = (post.userHasLiked);

        likes = post.likes;
        initializeLikesView();

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLikesView();
            }
        });

        if(post.imageUrl != null)
            Glide.with(this).load(post.imageUrl).into(ivImage);
    }

    // updates the view to accurately represent number of likes and fill in heart
    void updateLikesView(){

        if(userHasLiked){
            likes++;
            ivLike.setImageResource(R.drawable.ufi_heart_active);
        } else{
            likes--;
            ivLike.setImageResource(R.drawable.ufi_heart);
        }
        tvLikes.setText(Integer.toString(likes));

        if(likes == 1)
            tvWordLike.setText("like");
        else
            tvWordLike.setText("likes");

        userHasLiked = !userHasLiked;
    }

    void initializeLikesView(){
        if(userHasLiked){
            ivLike.setImageResource(R.drawable.ufi_heart_active);
        } else{
            ivLike.setImageResource(R.drawable.ufi_heart);
        }
        tvLikes.setText(Integer.toString(likes));

        if(likes == 1)
            tvWordLike.setText("like");
        else
            tvWordLike.setText("likes");

        userHasLiked = !userHasLiked;
    }


}
