package com.javierovico.ici.extra;

import android.content.SharedPreferences;

import com.javierovico.ici.extra.entities.AccessToken;


public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(AccessToken token){
        editor.putString("ACCESS_TOKEN", token.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN", token.getRefreshToken()).commit();
        editor.putBoolean("ES_DOCTOR",token.getEsDoctor()).commit();
        editor.putString("EXPIRACION",token.getExpiracion()).commit();
        editor.putBoolean("ES_ADMIN",token.getEsAdmin()).commit();
    }

    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
        editor.remove("ES_DOCTOR").commit();
        editor.remove("EXPIRACION").commit();
        editor.remove("ES_ADMIN").commit();
    }

    public AccessToken getToken(){
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("ACCESS_TOKEN", null));
        token.setRefreshToken(prefs.getString("REFRESH_TOKEN", null));
        token.setEsDoctor(prefs.getBoolean("ES_DOCTOR",false));
        token.setExpiracion(prefs.getString("EXPIRACION",null));
        token.setEsAdmin(prefs.getBoolean("ES_ADMIN",false));
        return token;
    }



}
