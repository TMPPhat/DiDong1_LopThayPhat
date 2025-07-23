package com.example.tranminhphat_2123110213;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ADDRESS = "address"; // 🔥 MỚI

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(User user) {
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_BIRTHDAY, user.getBirthday());
        editor.putString(KEY_IMAGE_URL, user.getImageUrl());
        editor.putString(KEY_FULLNAME, user.getFullname());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_ADDRESS, user.getAddress()); // 🔥 MỚI
        editor.apply();
    }

    public User getUser() {
        int id = sharedPreferences.getInt(KEY_ID, -1);
        String name = sharedPreferences.getString(KEY_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String phone = sharedPreferences.getString(KEY_PHONE, "");
        String birthday = sharedPreferences.getString(KEY_BIRTHDAY, "");
        String imageUrl = sharedPreferences.getString(KEY_IMAGE_URL, "");
        String fullname = sharedPreferences.getString(KEY_FULLNAME, "");
        String gender = sharedPreferences.getString(KEY_GENDER, "");
        String address = sharedPreferences.getString(KEY_ADDRESS, ""); // 🔥 MỚI

        return new User(id, name, email, phone, birthday, imageUrl, fullname, gender, address);
    }

    public void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }




    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_ID) && sharedPreferences.getInt(KEY_ID, -1) != -1;
    }
}
