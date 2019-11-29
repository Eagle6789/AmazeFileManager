package com.naga.filemanager.activities.superclasses;

import androidx.appcompat.app.AppCompatActivity;

import com.naga.filemanager.ui.colors.ColorPreferenceHelper;
import com.naga.filemanager.utils.application.AppConfig;
import com.naga.filemanager.utils.provider.UtilitiesProvider;
import com.naga.filemanager.utils.theme.AppTheme;

/**
 * Created by rpiotaix on 17/10/16.
 */
public class BasicActivity extends AppCompatActivity {

    protected AppConfig getAppConfig() {
        return (AppConfig) getApplication();
    }

    public ColorPreferenceHelper getColorPreference() {
        return getAppConfig().getUtilsProvider().getColorPreference();
    }

    public AppTheme getAppTheme() {
        return getAppConfig().getUtilsProvider().getAppTheme();
    }

    public UtilitiesProvider getUtilsProvider() {
        return getAppConfig().getUtilsProvider();
    }
}
