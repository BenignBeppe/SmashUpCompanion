package com.github.benignbeppe.smashupcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sebastian on 2015-11-14.
 */
public class Player extends LinearLayout implements View.OnClickListener,
        DialogInterface.OnClickListener {
    private MainActivity activity;
    private String name;
    private int points;
    private TextView nameView;
    private TextView pointsView;

    public Player(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity)context;
        points = 0;
        activity.addPlayer(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameView = (TextView)findViewById(R.id.playerName);
        Button editButton = (Button)findViewById(R.id.edit);
        editButton.setOnClickListener(this);
        Button decreaseButton = (Button)findViewById(R.id.decrease);
        decreaseButton.setOnClickListener(this);
        Button increaseButton = (Button)findViewById(R.id.increase);
        increaseButton.setOnClickListener(this);
        pointsView = (TextView)findViewById(R.id.points);
    }

    void changeName(String name) {
        this.name = name;
        nameView.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.decrease:
                changePoints(points - 1);
                break;
            case R.id.increase:
                changePoints(points + 1);
                break;
            case R.id.edit:
                showEditDialogue();
                break;
        }
    }

    public void changePoints(int points) {
        this.points = points;
        pointsView.setText(String.valueOf(points));
        Log.d(getClass().getSimpleName(), "Changed points for player '" + name +
                "' to " + points);
    }

    private void showEditDialogue() {
        AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(R.string.editPlayerTitle);
        dialogBuilder.setPositiveButton(android.R.string.ok, this);
        dialogBuilder.setNegativeButton(android.R.string.cancel, this);
        View dialogueView = View.inflate(
                activity, R.layout.edit_player_dialogue, null);
        dialogBuilder.setView(dialogueView);
        TextView nameView = (TextView)dialogueView.findViewById(R.id.name);
        nameView.setText(name);
        dialogBuilder.create();
        dialogBuilder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        AlertDialog dialogue = ((AlertDialog)dialog);
        TextView nameView = (TextView)dialogue.findViewById(R.id.name);
        if(which == DialogInterface.BUTTON_POSITIVE) {
            CheckBox deletePlayerCheckbox =
                (CheckBox)dialogue.findViewById(R.id.delete);
            if(deletePlayerCheckbox.isChecked()) {
                activity.removePlayer(this);
                ((ViewGroup)getParent()).removeView(this);
            }
            else {
                changeName(nameView.getText().toString());
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
