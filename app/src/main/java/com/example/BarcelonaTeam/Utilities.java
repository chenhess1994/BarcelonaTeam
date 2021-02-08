package com.example.BarcelonaTeam;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.acl.LastOwnerException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * static class that holds the all players and all matches
 */
public class  Utilities {

    private static String file_path;
    private static Context context_this;
    public static ArrayList<Player> allPlayers;
    public static ArrayList<Game> matches;

    /**
     * get image Id by the player name
     * @param player
     * @return image id
     */
    public static int getImageIdByPlayer(Player player){
        String nameIdentifier = (player.getFirstName() + "_" + player.getLastName()).toLowerCase();//this is the shape of name in drawable.
        int id = context_this.getResources().getIdentifier(nameIdentifier, "drawable", context_this.getPackageName());
        if(id==0)
            return R.drawable.player_defulat;
        return id;
    }

    /**
     * return the current context and
     * @param context
     */
    public static void InitializingData(Context context) {
        context_this = context;
        file_path = context.getFilesDir().getAbsolutePath();

        //The initializing data of the players.
        String stringOfInitializePlayers = playersStringData();

        //if the file is empty write the initializing data (so we are in the starting app)
        String isEmptyStringTest=readFile("log_allPlayers.txt");
        if(isEmptyStringTest.equals(""))
            writeData("log_allPlayers.txt", stringOfInitializePlayers);

        allPlayers =  parsePlayers();
        matches    =  MatchesXMLParser.parseMatches(context);
    }

    /**
     * read players from file
     * @return players from the file "log_allPlayers.txt".
     */
    public static ArrayList<Player> parsePlayers()
    {
        ArrayList<Player> players=new ArrayList<>();
        String preEditPlayers=Utilities.readFile("log_allPlayers.txt");
        if(preEditPlayers.equals(""))
            return players;
        ArrayList<String> allPlayersSplit = new ArrayList<>(Arrays.asList(preEditPlayers.split("AAA")));
        for(String player:allPlayersSplit){
            ArrayList<String> currentPlayer=new ArrayList<>(Arrays.asList(player.split("\n")));
            if(currentPlayer.size()!=0) {
                Player p = new Player();
                p.setFirstName(currentPlayer.get(0));
                p.setLastName(currentPlayer.get(1));
                p.setBirth(currentPlayer.get(2));
                p.setNationalTeam(currentPlayer.get(3));
                p.setGoals(currentPlayer.get(4));
                p.setAssists(currentPlayer.get(5));
                p.setSaves(currentPlayer.get(6));
                p.setImgPlayer(Utilities.getImageIdByPlayer(p));

                players.add(p);
            }
        }
        return players;
    }

    /**
     * read the file
     * @return String of the file
     */
    public static String readFile(String FILE_NAME) {
        StringBuilder text= null;
        String line;
        //Get the text file
        File file = new File(file_path,File.separator+FILE_NAME);
        try {
            if(!file.exists())
                file.createNewFile();

            //Read text from file
            InputStream inputStream=new FileInputStream(file);
            text = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            inputStream.close();
            br.close();
        } catch (IOException e) {
            Log.i("test","error");
            e.printStackTrace();
        }

        return text != null ? text.toString() : null;
    }

    /**
     * write the data to the log file
     * @param data the data is insert to the file
     * @param FILE_NAME the name of the file.
     */
    public static void writeData(String FILE_NAME,String data) {
        File directory = new File(file_path);
        if(!directory.exists())
            directory.mkdir();

        File newFile = new File(file_path,File.separator+FILE_NAME);
        try  {
            if(!newFile.exists())
                newFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(newFile,true);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
            outputWriter.write(data);
            if(FILE_NAME.equals("log_eleven.txt"))
                outputWriter.write("\n");
            outputWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * clean the log file
     */
    public static void cleanFile(String FILE_NAME){

        try {
            FileOutputStream writer = new FileOutputStream(file_path+File.separator+FILE_NAME);
            writer.write(("").getBytes());
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * remove player from log file
     * @param fullNamePlayer
     */
    public static void removePlayerFromLogLineUp(String fullNamePlayer){
        ArrayList<String> lineUpTemp = new ArrayList<>(Arrays.asList(Utilities.readFile("log_eleven.txt").split("\n")));
        lineUpTemp.remove(Utilities.getPlayer(fullNamePlayer).getPosition()+"-"+fullNamePlayer);
        cleanFile("log_eleven.txt");
        for (String str: lineUpTemp) {
            writeData("log_eleven.txt",str);
        }
    }

    /**
     * remove player from log file
     * @param fullNamePlayer
     */
    public static void removePlayerFromAllplayers(String fullNamePlayer){
        ArrayList<Player> players=new ArrayList<>();
        String preEditPlayers=Utilities.readFile("log_allPlayers.txt");
        ArrayList<String> allPlayersSplit = new ArrayList<>(Arrays.asList(preEditPlayers.split("AAA")));

        for(int i=0;i<allPlayersSplit.size();i++) {
            ArrayList<String> currentPlayer = new ArrayList<>(Arrays.asList(allPlayersSplit.get(i).split("\n")));
            if((currentPlayer.get(0)+" "+currentPlayer.get(1)).equals(fullNamePlayer)) {
                allPlayersSplit.remove(i);
                break;
            }
        }

//        for(int i=0;i<allPlayersSplit.size();i++) {
//            ArrayList<String> currentPlayer = new ArrayList<>(Arrays.asList(allPlayersSplit.get(i).split("\n")));
//            players.add(getPlayer((currentPlayer.get(0) + " " + currentPlayer.get(1))));
//        }

        cleanFile("log_allPlayers.txt");
        for (int i=0;i<allPlayersSplit.size()-1;i++) {
            writeData("log_allPlayers.txt", allPlayersSplit.get(i));
            writeData("log_allPlayers.txt", "AAA");
        }

        //if(allPlayersSplit.size()>0)
            writeData("log_allPlayers.txt", allPlayersSplit.get(allPlayersSplit.size()-1));

       // return  players;
    }

    /**
     * get the full name of the player
     * @param p the specific player
     * @return the player.
     */
    public static String getFullName(Player p){
        return  p.getFirstName() + " " + p.getLastName();
    }

    /**
     * this function return player on a string input
     * @param player the specific player
     * @return the player if found and null if not.
     */
    public static Player getPlayer(String player){
        for (Player p:allPlayers) {
            if(Utilities.getFullName(p).equals(player))
                return p;
        }
        return null;
    }

    /**
     * Function that separated the readFile function into map
     * that the key is the position of the player
     * and the value is the player
     * @return map <position,player>
     */
    public static HashMap readPlayersForLineUp(){
        int end;
        HashMap<String,String> playersList=new HashMap<>();
       ArrayList<String> list=new ArrayList<>(Arrays.asList(Utilities.readFile("log_eleven.txt").split("\n")));
       String strPosition =new String();
       String strName=new String();
       for(int i=0;i<list.size();i++)
       {
           end=list.get(i).indexOf("-");

           if(end==-1)
               return null;

           strPosition=list.get(i).substring(0,end);
           strName=list.get(i).substring(end+1);
           playersList.put(strPosition,strName);
       }
       return playersList;
    }

    /**
     * find the position of specific player from the pitch
     * @param p
     * @return the position of the player
     */
    public static String findPositionInPitch(Player p){
        String pos = null;
        //find the position of the player in the pitch.
        HashMap<String,String> lineUp=Utilities.readPlayersForLineUp();
        for(Map.Entry<String, String> player: lineUp.entrySet()){
            if(player.getValue().equals(Utilities.getFullName(p)))
            {
                pos=player.getKey();
            }
        }
        return pos;
    }

    /**
     * @return the string of the players
     */
    public static  String playersStringData(){
        //AAA separated between players, and \n separated between the items in the games.
        String data=
                    "Anssumane\n" +  "Fati\n"        +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Arturo\n"    +  "Vidal\n"       +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Clement\n"   +  "Lenglet\n"     +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Frenkie\n"   +  "De_jong\n"     +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Francisco\n" +  "Trincao\n"     +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Lional\n"    +  "Messi\n"       +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Gerard\n"    +  "Pique\n"       +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Jordy\n"     +  "Alba\n"        +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Jean\n"      +  "Todibo\n"      +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Junior\n"    +  "Firpo\n"       +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Luis\n"      +  "Suarez\n"      +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Marc\n"      +  "TerStegen\n"   +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Antoine\n"   +  "Griezmann\n"   +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Martin\n"    +  "Braithwaite\n" +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Miralem\n"   +  "Pianic\n"      +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Nelson\n"    +  "Semedo\n"      +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Noberto\n"   +  "Neto\n"        +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Ousmane\n"   +  "Dembele\n"     +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Philippe\n"  +  "Coutinho\n"    +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Rafael\n"    +  "Alcantara\n"   +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Riqui\n"     +  "Puig\n"        +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Samuel\n"    +  "Umtiti\n"      +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Sergi\n"     +  "Roberto\n"     +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Sergio\n"    +  "Busquets\n"    +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320\n" + "AAA" +
                    "Sergirio\n"  +  "Dest\n"        +  "20-09-1984\n" + "Argentina\n" +"500\n" +"400\n" + "320";

        return  data;

    }
}