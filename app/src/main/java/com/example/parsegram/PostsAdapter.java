package com.example.parsegram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private ParseUser user;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
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
        TextView tvUsername;
        TextView tvUsername2;
        TextView tvDescription;
        TextView tvLikes;
        TextView tvWordLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvWordLike = itemView.findViewById(R.id.tvWordLikes);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());

            //post.zeroLikes();
            post.addLike(ParseUser.getCurrentUser());
            int likes = post.getLikes();
            tvLikes.setText(Integer.toString(post.getLikes()));
            if(likes == 1)
                tvWordLike.setText("like");

            ParseFile image = post.getImage();
            if(image != null)
                Glide.with(context).load(image.getUrl()).into(ivImage);
        }

        void userLiked(){

        }

        void userUnliked(){

        }
    }
}
