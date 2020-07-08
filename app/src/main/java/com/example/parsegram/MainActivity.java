package com.example.parsegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parsegram.fragments.ComposeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Begin the transaction
        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment composeFragment = new ComposeFragment();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.actiion_compose:
                        fragment = composeFragment;
                        Toast.makeText(MainActivity.this, "Compose!!", Toast.LENGTH_SHORT).show();
                    case R.id.action_profile:
                        // TODO: Update frag
                        fragment = composeFragment;
                        Toast.makeText(MainActivity.this, "Profile!!", Toast.LENGTH_SHORT).show();
                    case R.id.action_home:
                    default:
                        // TODO: Update frag
                        fragment = composeFragment;
                        Toast.makeText(MainActivity.this, "Home!!", Toast.LENGTH_SHORT).show();
                }

                // Replace the contents of the container with the new fragment
                // using ft.replace(R.id.flContainer, new FooFragment());
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Complete the changes added above using commit
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.actiion_compose);
        //queryPosts();
    }


}