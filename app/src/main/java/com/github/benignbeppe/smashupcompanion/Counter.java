package com.github.benignbeppe.smashupcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sebastian on 2015-11-14.
 */
public abstract class Counter extends LinearLayout implements View.OnClickListener,
        DialogInterface.OnClickListener {
    protected MainActivity activity;
    protected String name;
    protected int value;
    protected TextView nameView;
    protected TextView valueView;

    public Counter(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity)context;
        value = 0;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameView = (TextView)findViewById(R.id.name);
        Button editButton = (Button)findViewById(R.id.edit);
        editButton.setOnClickListener(this);
        Button decreaseButton = (Button)findViewById(R.id.decrease);
        decreaseButton.setOnClickListener(this);
        Button increaseButton = (Button)findViewById(R.id.increase);
        increaseButton.setOnClickListener(this);
        valueView = (TextView)findViewById(R.id.value);
    }

    void changeName(String name) {
        this.name = name;
        nameView.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.decrease:
                changeValue(value - 1);
                break;
            case R.id.increase:
                changeValue(value + 1);
                break;
            case R.id.edit:
                showEditDialogue();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        AlertDialog dialogue = ((AlertDialog)dialog);
        if(which == DialogInterface.BUTTON_POSITIVE) {
            updateDataFromDialogue(dialogue);
        }
    }

    protected abstract void updateDataFromDialogue(AlertDialog dialogue);

    public void changeValue(int value) {
        this.value = value;
    }

    protected abstract void showEditDialogue();

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
