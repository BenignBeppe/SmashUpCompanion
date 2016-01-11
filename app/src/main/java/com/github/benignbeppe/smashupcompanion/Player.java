package com.github.benignbeppe.smashupcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sebastian on 2015-11-27.
 */
public class Player extends Counter {
    public Player(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity.addPlayer(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == R.id.increase) {
            Sound.play(activity, R.raw.point);
        }
    }

    @Override
    public void changeValue(int value) {
        super.changeValue(value);
        valueView.setText(String.valueOf(value));
        Log.d(getClass().getSimpleName(), "Changed points for player '" + name +
                "' to " + value);
    }

    @Override
    protected void showEditDialogue() {
        AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(R.string.editPlayerTitle);
        dialogBuilder.setPositiveButton(android.R.string.ok, this);
        dialogBuilder.setNegativeButton(android.R.string.cancel, this);
        View dialogue = View.inflate(
                activity, R.layout.edit_player_dialogue, null);
        dialogBuilder.setView(dialogue);
        TextView nameView = (TextView)dialogue.findViewById(R.id.name);
        nameView.setText(name);
        dialogBuilder.create();
        dialogBuilder.show();
    }

    @Override
    protected void updateDataFromDialogue(AlertDialog dialogue) {
        CheckBox deletePlayerCheckbox =
                (CheckBox)dialogue.findViewById(R.id.delete);
        if(deletePlayerCheckbox.isChecked()) {
            activity.removePlayer(this);
            ((ViewGroup)getParent()).removeView(this);
        }
        else {
            TextView nameView = (TextView)dialogue.findViewById(R.id.name);
            changeName(nameView.getText().toString());
        }
    }

    public JSONObject toJson() throws JSONException {
        JSONObject Json = new JSONObject();
        Json.put("name", getName());
        Json.put("value", getValue());
        return Json;
    }
}
