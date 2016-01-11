package com.github.benignbeppe.smashupcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sebastian on 2015-11-27.
 */
public class Base extends Counter {
    private int breakPoint;

    public Base(Context context, AttributeSet attrs) {
        super(context, attrs);
        breakPoint = 0;
        activity.addBase(this);
    }

    @Override
    public void changeValue(int value) {
        super.changeValue(value);
        updateValue();
    }

    private void updateValue() {
        valueView.setText(String.valueOf(value) + "/" + breakPoint);
    }

    @Override
    protected void showEditDialogue() {
        AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(R.string.editBaseTitle);
        dialogBuilder.setPositiveButton(android.R.string.ok, this);
        dialogBuilder.setNegativeButton(android.R.string.cancel, this);
        View dialogueView = View.inflate(
                activity, R.layout.edit_base_dialogue, null);
        dialogBuilder.setView(dialogueView);
        TextView nameView = (TextView)dialogueView.findViewById(R.id.name);
        nameView.setText(name);
        TextView breakPointView =
                (TextView)dialogueView.findViewById(R.id.breakPoint);
        breakPointView.setText(Integer.toString(breakPoint));
        dialogBuilder.create();
        dialogBuilder.show();
    }

    @Override
    protected void updateDataFromDialogue(AlertDialog dialogue) {
        TextView nameView = (TextView)dialogue.findViewById(R.id.name);
        changeName(nameView.getText().toString());
        TextView breakPointView =
                (TextView)dialogue.findViewById(R.id.breakPoint);
        changeBreakPoint(Integer.parseInt(breakPointView.getText().toString()));
    }

    public void changeBreakPoint(int breakPoint) {
        this.breakPoint = breakPoint;
        updateValue();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject Json = new JSONObject();
        Json.put("name", getName());
        Json.put("value", getValue());
        Json.put("breakPoint", getBreakPoint());
        return Json;
    }

    public int getBreakPoint() {
        return breakPoint;
    }
}
