package com.digitalisma.boilerplate.devsettings;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.digitalisma.boilerplate.R;
import com.digitalisma.boilerplate.ui.other.ViewModifier;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivityViewModifier implements ViewModifier {

    @NonNull
    @Override
    public <T extends View> T modify(@NonNull T view) {
        // Basically, what we do here is adding a Developer Setting Fragment to a DrawerLayout!
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.main_drawer_layout);

        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.gravity = Gravity.END;

        drawerLayout.addView(LayoutInflater.from(view.getContext()).inflate(R.layout.view_developer_settings, drawerLayout, false), layoutParams);
        return view;
    }

}