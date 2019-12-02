package com.naga.filemanager.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.RequiresApi;

import com.naga.filemanager.R;
import com.naga.filemanager.activities.MainActivity;
import com.naga.filemanager.adapters.data.LayoutElementParcelable;
import com.naga.filemanager.fragments.MainFragment;
import com.naga.filemanager.utils.MainActivityHelper;
import com.naga.filemanager.utils.provider.UtilitiesProvider;

public class CreateSomethingPopupMenu extends PopupMenu implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "CreateSomethingPopupMenu";

    private Context context;
    private MainActivity mainActivity;

    public CreateSomethingPopupMenu(Context context, View anchor,MainActivity mainActivity ) {
        super(context, anchor);
        this.mainActivity = mainActivity;

        // This is very important for listening on selected items
        setOnMenuItemClickListener(this);
    }



    @SuppressLint("LongLogTag")
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.new_folder_popup_menu_item:
                mainActivity.mainActivityHelper.add(mainActivity.mainActivityHelper.NEW_FOLDER);
                Log.d(TAG, "onMenuItemClick: New folder ");

                break;

            case R.id.new_file_popup_menu_item:
                mainActivity.mainActivityHelper.add(mainActivity.mainActivityHelper.NEW_FILE);
                Log.d(TAG, "onMenuItemClick: New folder ");

                break;

            case R.id.new_cloud_popup_menu_item:
                mainActivity.mainActivityHelper.add(mainActivity.mainActivityHelper.NEW_CLOUD);
                Log.d(TAG, "onMenuItemClick: New folder ");

                break;
            default:
                mainActivity.mainActivityHelper.add(mainActivity.mainActivityHelper.NEW_FOLDER);
                Log.d(TAG, "onMenuItemClick: Default");

                return false;


        }
        return false;
    }
}
