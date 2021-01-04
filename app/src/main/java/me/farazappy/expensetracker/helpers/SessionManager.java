package me.farazappy.expensetracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import me.farazappy.expensetracker.models.User;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ExpenseTracker";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";


    private static final String KEY_USER = "userJson";

    public SessionManager(Context context) {
        this._context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(_context);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();
    }

    public void setUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        editor.putString(KEY_USER, userJson);

        editor.commit();
    }

    public User getUser() {

        String userJson = pref.getString(KEY_USER, null);
        Gson gson = new Gson();

        return gson.fromJson(userJson, User.class);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void logOut(boolean isLoggedIn, User user) {
        this.setLogin(isLoggedIn);
        this.setUser(user);
    }
}
