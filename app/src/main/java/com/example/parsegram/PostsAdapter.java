package com.example.parsegram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private ParseUser user;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
        this.user = ParseUser.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        ImageView ivLike;
        TextView tvUsername;
        TextView tvUsername2;
        TextView tvDescription;
        TextView tvLikes;
        TextView tvWordLike;
        boolean userHasLiked;
        Likes userLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivLike = itemView.findViewById(R.id.ivLikeBtn);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvWordLike = itemView.findViewById(R.id.tvWordLikes);
        }

        public void bind(final Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            userHasLiked = !(post.userHasLikedPost());
            updateLikesView(post);
            for (ParseUser user: post.usersLiked) {
                Log.i("LIKES ON POST",  user.getUsername() + " likes "  + post.getDescription() + post.userHasLikedPost());
            }


            //post.zeroLikes();
            //post.addLike(ParseUser.getCurrentUser());

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userHasLiked = !userHasLiked;
                    if(userHasLiked){
                        saveUnlike(post);
                    } else{
                        saveLike(post);
                    }
                    updateLikesView(post);
                }
            });

            ParseFile image = post.getImage();
            if(image != null)
                Glide.with(context).load(image.getUrl()).into(ivImage);
        }

        // updates the view to accurately represent number of likes and fill in heart
        void updateLikesView(Post post){
            int likes = post.getNumberOfLikes();
            tvLikes.setText(Integer.toString(likes));

            if(!userHasLiked){
                ivLike.setImageResource(R.drawable.ufi_heart_active);
            } else{
                ivLike.setImageResource(R.drawable.ufi_heart);
            }

            if(likes == 1)
                tvWordLike.setText("like");
            else
                tvWordLike.setText("likes");
        }

        // This function send ADD query to Likes.class table and sets hasLiked boolean to true
        // Adds user who liked to LikedUser User<List> for post
        void saveLike(Post post) {
            userLike = new Likes();
            userLike.setPost(post);
            userLike.setUser(ParseUser.getCurrentUser());

            userLike.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        Log.e("SaveLikes:", "Issue with creating post!", e);
                        return;
                    }
                    Log.i("Likes:", "Post was liked");
                }
            });

            // Adds user to Post's List<user>
            post.addLike(user);
        }


        // This function send DELETE query to Likes.class table and sets hasLiked boolean to true
        // Adds user who liked to LikedUser User<List> for post
        void saveUnlike(Post post) {

            ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);

            // find row in Likes.class that contains post and user
            query.whereEqualTo(Likes.KEY_POST, post);
            query.whereEqualTo(Likes.KEY_USER, ParseUser.getCurrentUser());

            query.findInBackground(new FindCallback<Likes>() {
                @Override
                public void done(List<Likes> objects, ParseException e) {
                    if(e!=null){
                        Log.e("SaveUnlike:", "Issue finding liked post in Likes.class", e);
                        return;
                    }

                    // delete row from Likes.class table
                    for(Likes like: objects) {
                        Log.i("Likes:", "Before delete: Post was disliked " + objects.size());
                        like.deleteInBackground();
                        Log.i("Likes:", "After delete: Post was disliked " + objects.size());
                    }
                }
            });

            // deletes user from Post's List<user>
            post.removeLike(user);
        }
    }
}
