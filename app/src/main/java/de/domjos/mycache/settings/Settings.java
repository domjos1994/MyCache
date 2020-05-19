package de.domjos.mycache.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.util.Map;
import java.util.Objects;

public class Settings {
    private SharedPreferences sharedPreferences;

    public Settings(Context context) throws Exception {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        this.sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public <T> void saveSetting(String key, T value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        if(value instanceof String) {
            editor.putString(key, (String) value);
        } else if(value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if(value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if(value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if(value instanceof Double) {
            editor.putString(key, String.valueOf(value));
        } else if(value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            editor.putString(key, String.valueOf(value));
        }
        editor.apply();
    }

    @SuppressWarnings("unchecked")
    public <T> T getSetting(String key, T def) {
        Map<String, ?> all = this.sharedPreferences.getAll();
        if(all.containsKey(key)) {
            if(def instanceof Double) {
                Object obj = Double.parseDouble(Objects.requireNonNull(all.get(key)).toString());
                return (T) obj;
            } else {
                return (T) all.get(key);
            }
        } else {
            return def;
        }
    }
}
