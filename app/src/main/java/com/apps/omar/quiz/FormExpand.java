package com.apps.omar.quiz;

import android.content.Context;
import android.view.ContextMenu;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by omar on 01.12.16.
 */

public class FormExpand {
    private Context context;

    public FormExpand(Context context)
    {
        this.context = context;
    }

    public EditText checkForm(EditText child, ViewGroup parent)
    {
        if(child.getText().length() > 0)
        {
            if(child == parent.getChildAt(parent.getChildCount() - 1))
            {
                EditText newForm = new EditText(this.context);
                parent.addView(newForm);
                return newForm;
            }
        }
        else if(child.getText().length() == 0)
        {
            EditText lastView = (EditText) parent.getChildAt(parent.getChildCount() - 1);

            if(child != lastView)
            {
                parent.removeView(child);
                lastView.requestFocus();
            }
        }
        return null;
    }

}


