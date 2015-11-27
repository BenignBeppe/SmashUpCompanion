package com.github.benignbeppe.smashupcompanion;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        players = new ArrayList<>();
        Font.init(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button addPlayerButton = (Button)findViewById(R.id.addPlayerButton);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlayer();
            }
        });
        if(savedInstanceState != null) {
            ArrayList<String> playerNames =
                    savedInstanceState.getStringArrayList("playerNames");
            ArrayList<Integer> playerPoints =
                    savedInstanceState.getIntegerArrayList("playerPoints");
            for(int i = 0; i < playerNames.size(); i++) {
                createPlayer(playerNames.get(i), playerPoints.get(i));
            }
        }
    }

    private void createPlayer(String name, int points) {
        Log.d(getClass().getSimpleName(), "Adding player: '" + name + "'");
        ViewGroup parent;
        if(getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            if(players.size() < 2) {
                parent = (ViewGroup)findViewById(R.id.playersRow1);
            }
            else {
                parent = (ViewGroup)findViewById(R.id.playersRow2);
            }
        }
        else {
            parent = (ViewGroup)findViewById(R.id.playersArea);
        }
        View.inflate(this, R.layout.player, parent);
        Player addedPlayer = players.get(players.size() - 1);
        addedPlayer.changeName(name);
        addedPlayer.changePoints(points);
    }

    private void createPlayer() {
        String name = "Player " + (players.size() + 1);
        createPlayer(name, 0);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<Integer> playerPoints = new ArrayList<>();
        for(Player player: players) {
            playerNames.add(player.getName());
            playerPoints.add(player.getPoints());
        }
        outState.putStringArrayList("playerNames", playerNames);
        outState.putIntegerArrayList("playerPoints", playerPoints);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(getClass().getSimpleName(), "Restoring: " + savedInstanceState);

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
