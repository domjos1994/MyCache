package de.domjos.mycache.settings;

import android.content.Context;

import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

public class Globals {
    private Settings settings;
    private String ocPublicKey;
    private String ocSecretKey;
    private OkHttpOAuthConsumer consumer;

    public Globals() {
        this.settings = null;
    }

    public void setSettings(Context context) throws Exception {
        this.settings = new Settings(context);
        this.consumer = null;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public String getOcPublicKey() {
        return this.ocPublicKey;
    }

    public void setOcPublicKey(String ocPublicKey) {
        this.ocPublicKey = ocPublicKey;
    }

    public String getOcSecretKey() {
        return this.ocSecretKey;
    }

    public void setOcSecretKey(String ocSecretKey) {
        this.ocSecretKey = ocSecretKey;
    }

    public OkHttpOAuthConsumer getConsumer() {
        return this.consumer;
    }

    public void setConsumer(OkHttpOAuthConsumer consumer) {
        this.consumer = consumer;
    }
}
