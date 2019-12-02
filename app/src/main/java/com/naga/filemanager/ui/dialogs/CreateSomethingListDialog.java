package com.naga.filemanager.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.naga.filemanager.activities.superclasses.ThemedActivity;
import com.naga.filemanager.utils.MainActivityHelper;

public class CreateSomethingListDialog {

    private static final String TAG = "CreateSomethingListDialog";

    public static MaterialDialog show(Context context,
                                      @StringRes int title, MainActivityHelper mainActivityHelper
                                             ) {


        MaterialDialog.Builder a = new MaterialDialog.Builder(context)
                .title(title);


        String[] animals = {"New Folder"};

        a.items(animals);
        a.itemsCallback((dialog, itemView, position, text) -> {
            switch (position){
                case 0:
                    mainActivityHelper.add(0);
            }
        });

        return a.build();
    }

}
