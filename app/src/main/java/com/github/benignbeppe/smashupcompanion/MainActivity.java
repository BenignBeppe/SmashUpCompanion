package com.github.benignbeppe.smashupcompanion;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static int MAX_PLAYERS = 4;
    private final static String SESSION_FILE_NAME = "session.json";

    ArrayList<Player> players;
    ArrayList<Base> bases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        players = new ArrayList<>();
        bases = new ArrayList<>();
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
        createBase();
        if(savedInstanceState != null) {
            ArrayList<String> playerNames =
                    savedInstanceState.getStringArrayList("playerNames");
            ArrayList<Integer> playerPoints =
                    savedInstanceState.getIntegerArrayList("playerPoints");
            for(int i = 0; i < playerNames.size(); i++) {
                createPlayer(playerNames.get(i), playerPoints.get(i));
            }
            ArrayList<String> baseNames =
                    savedInstanceState.getStringArrayList("baseNames");
            ArrayList<Integer> baseValues =
                    savedInstanceState.getIntegerArrayList("baseValues");
            ArrayList<Integer> baseBreakPoints =
                    savedInstanceState.getIntegerArrayList("baseBreakPoints");
            for(int i = 0; i < bases.size(); i ++) {
                Base base = bases.get(i);
                base.changeName(baseNames.get(i));
                base.changeValue(baseValues.get(i));
                base.changeBreakPoint(baseBreakPoints.get(i));
            }
        }
        else {
            try {
                String sessionString = readSessionFile();
                loadSessionFromJson(sessionString);
            } catch(IOException | JSONException e) {
                e.printStackTrace();
                createPlayer("Player 1");
                createPlayer("Player 2");
            }
        }
    }

    private String readSessionFile() throws IOException {
        FileInputStream in = openFileInput(SESSION_FILE_NAME);
        int i;
        String sessionString = "";
        while((i = in.read()) != -1){
            char ch = (char)i;
            sessionString += ch;
        }
        Log.d(getClass().getName(), "Read session file: " + sessionString);
        return sessionString;
    }

    private void loadSessionFromJson(String sessionString)
            throws JSONException {
        JSONObject sessionJson = new JSONObject(sessionString);
        JSONArray playersJson = sessionJson.getJSONArray("players");
        for(int i = 0; i < playersJson.length(); i ++) {
            JSONObject playerJson = playersJson.getJSONObject(i);
            createPlayer(playerJson.getString("name"),
                    playerJson.getInt("value"));
        }
        JSONArray basesJson = sessionJson.getJSONArray("bases");
        for(int i = 0; i < bases.size(); i ++) {
            JSONObject baseJson = basesJson.getJSONObject(i);
            Base base = bases.get(i);
            base.changeName(baseJson.getString("name"));
            base.changeValue(baseJson.getInt("value"));
            base.changeBreakPoint(baseJson.getInt("breakPoint"));
        }
        Log.d(getClass().getName(), "Loaded session: " + sessionString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject session = null;
        try {
            session = createSessionJson();

        } catch(JSONException e) {
            e.printStackTrace();
        }
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(SESSION_FILE_NAME,
                    Context.MODE_PRIVATE);
            outputStream.write(session.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(getClass().getName(), "Wrote session file: " + session);
    }

    private JSONObject createSessionJson() throws JSONException {
        JSONObject sessionJson = new JSONObject();
        JSONArray playersJson = new JSONArray();
        for(Player player: players) {
            JSONObject playerJson = player.toJson();
            playersJson.put(playerJson);
        }
        sessionJson.put("players", playersJson);
        JSONArray basesJson = new JSONArray();
        for(Base base: bases) {
            JSONObject baseJson = base.toJson();
            basesJson.put(baseJson);
        }
        sessionJson.put("bases", basesJson);
        return sessionJson;
    }

    private void createPlayer(String name, int points) {
        Log.d(getClass().getSimpleName(), "Creating player: '" + name + "'");
        ViewGroup parent;
        if(isLandscape()) {
            if(players.size() > 1) {
                parent = (ViewGroup)findViewById(R.id.playerRow2);
            }
            else {
                parent = (ViewGroup)findViewById(R.id.playerRow1);
            }
        }
        else {
            parent = (ViewGroup)findViewById(R.id.playerArea);
        }
        View.inflate(this, R.layout.player, parent);
        Player addedPlayer = players.get(players.size() - 1);
        addedPlayer.changeName(name);
        addedPlayer.changeValue(points);
        createBase();
    }

    private void createBase(String name, int value, int breakPoint) {
        Log.d(getClass().getSimpleName(), "Creating base: '" + name + "'");
        ViewGroup parent;
        if(isLandscape()) {
            if(players.size() > 3) {
                parent = (ViewGroup)findViewById(R.id.baseRow3);
            }
            else if(players.size() > 1){
                parent = (ViewGroup)findViewById(R.id.baseRow2);
            }
            else {
                parent = (ViewGroup)findViewById(R.id.baseRow1);
            }
        }
        else {
            parent = (ViewGroup)findViewById(R.id.baseArea);
        }
        View.inflate(this, R.layout.base, parent);
        Base addedBase = bases.get(bases.size() - 1);
        addedBase.changeName(name);
        addedBase.changeValue(value);
        addedBase.changeBreakPoint(breakPoint);
    }

    private void createBase() {
        createBase("Base " + (bases.size() + 1), 0, 0);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
    }

    private void createPlayer(String name) {
        createPlayer(name, 0);
    }

    private void createPlayer() {
        String name = "Player " + (players.size() + 1);
        createPlayer(name);
    }

    public void addPlayer(Player player) {
        players.add(player);
        if(players.size() == MAX_PLAYERS) {
            Button addPlayerButton = (Button)findViewById(R.id.addPlayerButton);
            addPlayerButton.setEnabled(false);
        }
    }

    public void addBase(Base base) {
        bases.add(base);
    }

    public void removePlayer(Player player) {
        Log.d(getClass().getSimpleName(), String.format(
                "Removing player: '%s', %s", player.getName(), player));
        players.remove(player);
        Button addPlayerButton = (Button)findViewById(R.id.addPlayerButton);
        addPlayerButton.setEnabled(true);
        ViewGroup firstRow = (ViewGroup)findViewById(R.id.playerRow1);
        ViewGroup secondRow = (ViewGroup)findViewById(R.id.playerRow2);
        if(isLandscape() &&
                player.getParent() == firstRow &&
                secondRow.getChildCount() > 0) {
            View firstPlayerOnSecondRow = secondRow.getChildAt(0);
            secondRow.removeView(firstPlayerOnSecondRow);
            firstRow.addView(firstPlayerOnSecondRow);
        }
        removeBase();
    }

    private void removeBase() {
        Base base = bases.get(bases.size() - 1);
        Log.d(getClass().getSimpleName(), String.format(
                "Removing base: '%s', %s", base.getName(), base));
        ((ViewGroup)findViewById(R.id.baseArea)).removeView(base);
        bases.remove(base);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<Integer> playerPoints = new ArrayList<>();
        ArrayList<String> baseNames = new ArrayList<>();
        ArrayList<Integer> baseValues = new ArrayList<>();
        ArrayList<Integer> baseBreakPoints = new ArrayList<>();
        for(Player player: players) {
            playerNames.add(player.getName());
            playerPoints.add(player.getValue());
        }
        for(Base base: bases) {
            baseNames.add(base.getName());
            baseValues.add(base.getValue());
            baseBreakPoints.add(base.getBreakPoint());
        }
        outState.putStringArrayList("playerNames", playerNames);
        outState.putIntegerArrayList("playerPoints", playerPoints);
        outState.putStringArrayList("baseNames", baseNames);
        outState.putIntegerArrayList("baseValues", baseValues);
        outState.putIntegerArrayList("baseBreakPoints", baseBreakPoints);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(getClass().getSimpleName(), "Restoring instance: " +
                savedInstanceState);
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
