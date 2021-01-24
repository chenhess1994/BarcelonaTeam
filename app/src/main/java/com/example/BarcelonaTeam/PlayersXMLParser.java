package com.example.BarcelonaTeam;

import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PlayersXMLParser {
    final static String KEY_PLAYER="player";
    final static String KEY_FIRST_NAME="firstname";
    final static String KEY_LAST_NAME="lastname";
    final static String KEY_BIRTH="birth";
    final static String KEY_NATIONAL="nationalTeam";
    final static String KEY_GOALS="goals";
    final static String KEY_ASSISTS="assists";
    final static String KEY_SAVES="saves";


    public static ArrayList<Player> parsePlayers(Context context){
        ArrayList<Player> data = null;
        InputStream in = openPlayersFile(context);
        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            parser.setInput(in, null);
            int eventType = parser.getEventType();
            Player currentPlayer = null;
            String inTag = "";
            String strTagText = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                inTag = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        data = new ArrayList<Player>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (inTag.equalsIgnoreCase(KEY_PLAYER))
                            currentPlayer = new Player();
                        break;
                    case XmlPullParser.TEXT:
                        strTagText = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inTag.equalsIgnoreCase(KEY_PLAYER))
                            data.add(currentPlayer);
                        else if (inTag.equalsIgnoreCase(KEY_FIRST_NAME))
                            currentPlayer.setFirstName(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_LAST_NAME))
                            currentPlayer.setLastName(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_BIRTH))
                            currentPlayer.setBirth(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_NATIONAL))
                            currentPlayer.setNationalTeam(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_GOALS))
                            currentPlayer.setGoals(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_ASSISTS))
                            currentPlayer.setAssists(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_SAVES))
                            currentPlayer.setSaves(strTagText);
                        inTag ="";
                        break;
                }//switch
                eventType = parser.next();
            }//while
        } catch (Exception e) {e.printStackTrace();}
        return data;
    }

    private static InputStream openPlayersFile(Context context){
        AssetManager assetManager = context.getAssets();
        InputStream in =null;
        try {
            in = assetManager.open("players_data.xml");
        } catch (IOException e) {e.printStackTrace();}
        return in;
    }
}
