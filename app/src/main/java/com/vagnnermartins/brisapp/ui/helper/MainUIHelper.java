package com.vagnnermartins.brisapp.ui.helper;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.vagnnermartins.brisapp.R;

/**
 * Created by vagnnermartins on 19/03/15.
 */
public class MainUIHelper {

    public Toolbar toolbar;
    public TextView message;
    public View progress;

    public MainUIHelper(View view){
        this.toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        this.message = (TextView) view.findViewById(R.id.main_message);
        this.progress = view.findViewById(R.id.main_progress);
    }
}
