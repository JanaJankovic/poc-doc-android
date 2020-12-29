package feri.pora.pocket_doctor;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import feri.pora.datalib.User;
import feri.pora.pocket_doctor.config.ApplicationConfig;

public class ApplicationState extends Application {

    private static Gson gson;
    public static SharedPreferences sharedPreferences;

    private static User loggedUser;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public static Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }

    public static boolean checkLoggedUser(){
        if (sharedPreferences.contains(ApplicationConfig.USER_KEY)) {
            return true;
        }
        return false;
    }

    public static User loadLoggedUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = sharedPreferences.getString(ApplicationConfig.USER_KEY, "");
        loggedUser = getGson().fromJson(json, User.class);
        editor.apply();
        return loggedUser;
    }

    public static void saveLoggedUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = getGson().toJson(user);
        editor.putString(ApplicationConfig.USER_KEY, json);
        editor.apply();
    }
}
