package com.chaychan.bottombarlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void useVp(View view){
        startActivity(new Intent(this,ViewPagerActivity.class));
    }

    public void noUseVp(View view){
        startActivity(new Intent(this,FragmentManagerActivity.class));
    }
}
