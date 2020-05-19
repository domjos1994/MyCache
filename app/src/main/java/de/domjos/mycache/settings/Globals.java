package de.domjos.mycache.settings;

import android.content.Context;

public class Globals {
    private Settings settings;

    public Globals() {
        this.settings = null;
    }

    public void setSettings(Context context) throws Exception {
        this.settings = new Settings(context);
    }

    public Settings getSettings() {
        return this.settings;
    }
}
