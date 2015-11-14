package com.github.benignbeppe.smashupcompanion;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by sebastian on 2015-11-14.
 */
public class Player extends LinearLayout implements View.OnClickListener {
    private String name;
    private int points;
    private TextView nameView;
    private Button decreaseButton;
    private Button increaseButton;
    private TextView pointsView;

    public Player(Context context, AttributeSet attrs) {
        super(context, attrs);
        points = 0;
        ((MainActivity)context).addPlayer(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameView = (TextView)findViewById(R.id.playerName);
        decreaseButton = (Button)findViewById(R.id.decrease);
        decreaseButton.setOnClickListener(this);
        increaseButton = (Button)findViewById(R.id.increase);
        increaseButton.setOnClickListener(this);
        pointsView = (TextView)findViewById(R.id.points);
    }

    void changeName(String name) {
        this.name = name;
        nameView.setText(name);
    }

    @Override
    public void onClick(View v) {
        if(v == decreaseButton) {
            changePoints(points - 1);
        }
        else if(v == increaseButton) {
            changePoints(points + 1);
        }
    }

    public void changePoints(int points) {
        this.points = points;
        pointsView.setText(String.valueOf(points));
        Log.d(getClass().getSimpleName(), "Changed points for player '" + name +
                "' to " + points);
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

}
