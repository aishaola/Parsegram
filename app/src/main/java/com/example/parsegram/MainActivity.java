package com.example.parsegram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    ImageView ivPostImage;
    EditText etDescription;
    Button btnSubmit;
    Button btnCaptureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPostImage = findViewById(R.id.ivPostImage);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);

        queryPosts();
    }

    private void queryPosts(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "done: Issue with getting posts!", e);
                    return;
                }
                for(Post post: objects){
                    Log.i(TAG, "done: post: " + post.getDescription() + ", user: " + post.getUser().getUsername());
                }
            }
        });
    }
}