package com.example.efede.translator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static long backPressTime = 0;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {  //Check user login. if user login in application open main page or user not login open login page.
                if(firebaseAuth.getCurrentUser() == null){

                    Intent googleLoginIntent = new Intent(MainActivity.this,GoogleLoginActivity.class);
                    googleLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(googleLoginIntent);

                }else {
                    LifeCycler lifeCycler = new LifeCycler(MainActivity.this);
                    registerComponentCallbacks(lifeCycler);
                }
            }
        };

        toolbar = findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.main_tab);
        viewPager = findViewById(R.id.main_pager);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.sign_out){
            if(auth.getCurrentUser() != null){
                auth.signOut();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(backPressTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            if(auth.getCurrentUser() != null){
                auth.signOut();
            }
        }

        backPressTime = System.currentTimeMillis();
    }
}
