package com.example.parsegram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.text.format.DateUtils;
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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private ParseUser user;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
        this.user = ParseUser.getCurrentUser();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
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
        TextView tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivLike = itemView.findViewById(R.id.ivLikeBtn);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvWordLike = itemView.findViewById(R.id.tvWordLikes);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);



            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    showDetailsActivity();
                }
            });
        }

        void showDetailsActivity(){
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = posts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, DetailActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                //intent.putExtra("ViewHolder", Parcels.wrap(this));
                // show the activity
                context.startActivity(intent);
                post.initBoolAndLikes();
            }
        }

        public void bind(final Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            tvTimestamp.setText(post.getRelativeTimeAgo());

            updateLikesView(post);
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post.updateLike();
                    updateLikesView(post);
                }
            });

            ParseFile image = post.getImage();
            if(image != null)
                Glide.with(context).load(image.getUrl()).into(ivImage);
        }



        // updates the view to accurately represent number of likes and fill in heart
        void updateLikesView(Post post){
            if(post.userHasLiked){
                ivLike.setImageResource(R.drawable.ufi_heart_active);
            } else{
                ivLike.setImageResource(R.drawable.ufi_heart);
            }
            tvLikes.setText(Integer.toString(post.likes));
            if(post.likes == 1)
                tvWordLike.setText("like");
            else
                tvWordLike.setText("likes");
        }

    }
}
