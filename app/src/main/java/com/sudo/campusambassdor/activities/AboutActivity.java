package com.sudo.campusambassdor.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sudo.campusambassdor.R;

public class AboutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView final1 = findViewById(R.id.final1);
        ImageView final2 = findViewById(R.id.final2);
        ImageView final3 = findViewById(R.id.final3);
        ImageView final4 = findViewById(R.id.final4);

        Glide.with(this).load("https://s3-ap-southeast-1.amazonaws.com/blog.internshala.com/wp-content/uploads/2017/04/A-Students-Guide-to-Campus-Ambassador-Programs-672x332.jpg").into(final1);
        Glide.with(this).load("https://i.ytimg.com/vi/GXyV5YLju94/hqdefault.jpg").into(final2);
        Glide.with(this).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTldDnKDuUGMnj-rifexCPVzTvwX0PTEg_WN5D_hlyW6QiEz_az9A").into(final3);
        Glide.with(this).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVcJaxEbGlN8T6As_3Ily9UChwc3HMQWXgUG0Jw5gIwwMxo0Lm").into(final4);
    }
}
