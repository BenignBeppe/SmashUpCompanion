package com.github.benignbeppe.smashupcompanion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        players = new ArrayList<>();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createPlayer("Player 1");
        createPlayer("Player 2");
        createPlayer("Player 3");
        createPlayer("Player 4");
    }

    private void createPlayer(String name) {
        Log.d(getClass().getSimpleName(), "Adding player: '" + name + "'");
        ViewGroup playersArea = (ViewGroup)findViewById(R.id.playersArea);
        View.inflate(this, R.layout.player, playersArea);
        Player addedPlayer = players.get(players.size() - 1);
        addedPlayer.changeName(name);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
