package com.bytesnmaterials.zro.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.bytesnmaterials.zro.Constants;
import com.bytesnmaterials.zro.models.UserAuth;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is a shared preference class, which is used to store different values to app's shared preference.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public class SharedPreferenceServices {

    public SharedPreferenceServices() {
        super();
    }

    /**
     * This method is used to save logged in user to app's shared preferences.
     * @param context : context of application.
     * @param user : an instance of UserAuth data model class.
     */
    public void saveLoggedInUser(Context context, UserAuth user){

        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put(Constants.KEY_USER_ID, user.Id);
            jsonObject.put(Constants.KEY_USER_EMAIL, user.UserEmail);
            jsonObject.put(Constants.KEY_USER_STATUS, user.Status);
            jsonObject.put(Constants.KEY_USER_FULL_NAME, user.UserFullName);
            jsonObject.put(Constants.KEY_USER_NICK_NAME, user.UserDisplayName);
            jsonObject.put(Constants.KEY_AUTH_PROVIDER, user.AuthProvider);
            jsonObject.put(Constants.KEY_USER_UID, user.Uid);
            jsonObject.put(Constants.KEY_REGISTRATION_DATE_TIME, user.DateTime);
            jsonObject.put(Constants.KEY_COUNTRY, user.LocationCountry);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveText(context, Constants.KEY_LOGGED_IN_USER, jsonObject.toString());
    }

    /**
     * This method is used to retrieve the saved user from app's shared preferences.
     * @param context : context of application.
     */
    public UserAuth getLoggedInUserFromPreference(Context context){
        try {
            JSONObject jsonObject = new JSONObject(getText(context, Constants.KEY_LOGGED_IN_USER));
            UserAuth user = new UserAuth();
            //user.Id = jsonObject.get(Constants.KEY_USER_ID).toString();
            user.Uid = jsonObject.get(Constants.KEY_USER_UID).toString();
            user.UserEmail = jsonObject.get(Constants.KEY_USER_EMAIL).toString();
            user.Status = jsonObject.get(Constants.KEY_USER_STATUS).toString();
            user.UserFullName = jsonObject.get(Constants.KEY_USER_FULL_NAME).toString();
            user.UserDisplayName = jsonObject.get(Constants.KEY_USER_NICK_NAME).toString();
            user.AuthProvider = jsonObject.get(Constants.KEY_AUTH_PROVIDER).toString();
            user.DateTime = jsonObject.get(Constants.KEY_REGISTRATION_DATE_TIME).toString();
            user.LocationCountry = jsonObject.get(Constants.KEY_COUNTRY).toString();
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to clear the saved user from app's shared preferences.
     * @param context : context of application.
     */
    public void clearLoggedInUserToPreference(Context context){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(Constants.KEY_LOGGED_IN_USER, "");
        editor.commit();
    }

    /**
     * This method is used to save the text value along with the key passed in parameter to app's shared preferences.
     * @param context : context of application.
     * @param PREFS_KEY : Key name to store the value in shared preferences
     * @param text : String value which need to be stored.
     */
    public void saveText(Context context, String PREFS_KEY, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY, text);
        editor.commit();
    }

    /**
     * This method is used to save the boolean flag along with the key passed in parameter to app's shared preferences.
     * @param context : context of application.
     * @param PREFS_KEY : Key name to store the value in shared preferences
     * @param flag : boolean flag (true/false) value which need to be stored.
     */
    public void saveFlag(Context context, String PREFS_KEY, boolean flag) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putBoolean(PREFS_KEY, flag); //3

        editor.commit(); //4
    }

    /**
     * This method is used to get the text value saved for the key passed in parameter from app's shared preferences.
     * @param context : context of application.
     * @param PREFS_KEY : Key name to fetch the value from shared preferences.
     */
    public String getText(Context context, String PREFS_KEY) {
        SharedPreferences settings;
        String text;

        settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY, null);
        return text;
    }

    /**
     * This method is used to get the boolean flag value saved for the key passed in parameter from app's shared preferences.
     * @param context : context of application.
     * @param PREFS_KEY : Key name to fetch the value from shared preferences.
     */
    public boolean getFlagValue(Context context, String PREFS_KEY) {
        SharedPreferences settings;
        boolean flag;

        settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        flag = settings.getBoolean(PREFS_KEY, false);
        return flag;
    }

    /**
     * This method is used remove the value along with key passed in parameter from app's shared preferences.
     * @param context : context of application.
     * @param PREFS_KEY : Key name which need to be removed from shared preferences.
     */
    public void removeValue(Context context, String PREFS_KEY) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }
}