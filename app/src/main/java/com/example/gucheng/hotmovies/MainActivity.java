package com.example.gucheng.hotmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.example.gucheng.hotmovies.MainActivityFragment.highUrl;
import static com.example.gucheng.hotmovies.MainActivityFragment.popUrl;
import static com.example.gucheng.hotmovies.MainActivityFragment.tmdbUrl;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.fragment_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){                  // how to refresh the data and UI?
            case R.id.action_popular:
                Toast.makeText(getApplicationContext(),R.string.action_popular,Toast.LENGTH_SHORT).show();
                tmdbUrl = popUrl;
                break;
            case R.id.action_rates:
                Toast.makeText(getApplicationContext(),R.string.action_rates,Toast.LENGTH_SHORT).show();
                tmdbUrl = highUrl;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
