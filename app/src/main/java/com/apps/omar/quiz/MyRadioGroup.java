package com.apps.omar.quiz;

import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

/**
 * Created by omar on 02.12.16.
 */

public class MyRadioGroup {
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();


    public void addButton(RadioButton radioButton)
    {
        radioButtons.add(radioButton);
        radioButton.setOnClickListener(onClick);
    }

    public void removeButton(RadioButton radioButton)
    {
        radioButtons.remove(radioButton);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                for(RadioButton radioButton : radioButtons)
                {
                    radioButton.setChecked(false);
                }

            ((RadioButton) view).setChecked(true);
        }
    };
}
