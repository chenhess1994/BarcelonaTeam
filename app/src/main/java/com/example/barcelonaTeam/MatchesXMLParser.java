package com.example.barcelonaTeam;

import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MatchesXMLParser {
    final static String KEY_MATCH="match";
    final static String KEY_HOME="home";
    final static String KEY_AWAY="away";
    final static String KEY_DATE="date";
    final static String KEY_TIME="time";


    public static ArrayList<Game> parseMatches(Context context){
        ArrayList<Game> data = null;
        InputStream in = openPlayersFile(context);
        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            parser.setInput(in, null);
            int eventType = parser.getEventType();
            Game currentMatch = null;
            String inTag = "";
            String strTagText = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                inTag = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        data = new ArrayList<Game>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (inTag.equalsIgnoreCase(KEY_MATCH))
                            currentMatch = new Game();
                        break;
                    case XmlPullParser.TEXT:
                        strTagText = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inTag.equalsIgnoreCase(KEY_MATCH))
                            data.add(currentMatch);
                        else if (inTag.equalsIgnoreCase(KEY_HOME))
                            currentMatch.setHome(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_AWAY))
                            currentMatch.setAway(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_DATE))
                            currentMatch.setStrDate(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_TIME))
                            currentMatch.setStrTime(strTagText);
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
            in = assetManager.open("matches_details.xml");
        } catch (IOException e) {e.printStackTrace();}
        return in;
    }
}
