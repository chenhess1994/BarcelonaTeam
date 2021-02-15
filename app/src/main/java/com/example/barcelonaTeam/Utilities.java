package com.example.barcelonaTeam;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * static class that holds the all players and all matches
 * and useful functions
 */
public class  Utilities {

    private static String file_path; //file path that hold destination of used files
    private static Context context_this;
    public static ArrayList<Player> allPlayers; //Array to hold all the players in the system
    public static ArrayList<Game> matches;  //Array to hold all the scheduled matches of the team
    public static boolean flagForegroundNotification;   //Flag that will change according to the pressed option of notification
    /**
     * get image Id by the player object
     * @param player
     * @return image id
     */
    public static int getImageIdByPlayer(Player player){
        String nameIdentifier = (player.getFirstName() + "_" + player.getLastName()).toLowerCase();//this is the shape of name in drawable.
        int id = context_this.getResources().getIdentifier(nameIdentifier, "drawable", context_this.getPackageName());
        //If the system doesn't have an image corresponding to the player return the id of the default image
        if(id==0)
            return R.drawable.player_defulat;
        return id;
    }

    /**
     * Initializing the data for the system and this class
     * @param context
     */
    public static void InitializingData(Context context) {
        context_this = context;
        file_path = context.getFilesDir().getAbsolutePath();    //path of files

        //String to hold all the data of the players in one long string
        String stringOfInitializePlayers = playersStringData();

        //Read the file of all the players in the system
        String isEmptyStringTest=readFile("log_allPlayers.txt");
        /*
        If the file doesn't contain any players write all the data of the players again
        Happens on first launch and when you delete all the players
        */
        if(isEmptyStringTest.equals("")||isEmptyStringTest.equals("\n"))
            writeData("log_allPlayers.txt", stringOfInitializePlayers);
        /*
        Read all the data of the players and matches
         */
        allPlayers =  parsePlayers();
        matches    =  MatchesXMLParser.parseMatches(context);
    }

    /**
     * Read the data of the players from the file "log_allPlayers.txt"
     * @return The data in an ArrayList of players after cutting and creating the players objects
     */
    public static ArrayList<Player> parsePlayers()
    {
        //Create empty array
        ArrayList<Player> players=new ArrayList<>();
        //Read the data from the file
        String preEditPlayers=Utilities.readFile("log_allPlayers.txt");
        //If there wasn't any data return the empty array
        if(preEditPlayers.equals(""))
            return players;
        //Split the long string according to our accepted signature which is AAA
        ArrayList<String> allPlayersSplit = new ArrayList<>(Arrays.asList(preEditPlayers.split("AAA")));
        //Go over the split players and split the data of each player by the accepted sign of '\n'
        for(String player:allPlayersSplit){
            ArrayList<String> currentPlayer=new ArrayList<>(Arrays.asList(player.split("\n")));
            //If we were able to pull data from the string Create a player object and add it to the array
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
     * Long range saving using RAW file - reading
     * Generic function to read a file
     * @param FILE_NAME name of the file to read
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
     * Long range saving using RAW file - writing
     * Generic function to write  data to a file
     * @param data the data to write to the file
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
            //We want to write '\n' only to a certain file
            if(FILE_NAME.equals("log_eleven.txt"))
                outputWriter.write("\n");
            outputWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Long range saving using RAW file - deletion
     * Generic function to clean a file
     * @param FILE_NAME name of file
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
     * remove player from "log_eleven.txt" file
     * @param fullNamePlayer name of player to remove
     */
    public static void removePlayerFromLogLineUp(String fullNamePlayer){
        //Read the file and split the string
        ArrayList<String> lineUpTemp = new ArrayList<>(Arrays.asList(Utilities.readFile("log_eleven.txt").split("\n")));
        //Get the player object according to the given name
        String temp=Utilities.getPlayer(fullNamePlayer).getPosition();
        //Remove the player from the array
        lineUpTemp.remove(Utilities.getPlayer(fullNamePlayer).getPosition()+"-"+fullNamePlayer);
        //Clean the file
        cleanFile("log_eleven.txt");
        //Write all the players to the file
        for (String str: lineUpTemp) {
            writeData("log_eleven.txt",str);
        }
    }

    /**
     * remove player from "log_allPlayers.txt" file
     * @param fullNamePlayer name of player to remove
     */
    public static void removePlayerFromAllplayers(String fullNamePlayer){
        //Create empty array
        ArrayList<Player> players=new ArrayList<>();
        //Read file
        String preEditPlayers=Utilities.readFile("log_allPlayers.txt");
        //Split String
        ArrayList<String> allPlayersSplit = new ArrayList<>(Arrays.asList(preEditPlayers.split("AAA")));
        //Go over all the strings
        for(int i=0;i<allPlayersSplit.size();i++) {
            //Split each player data
            ArrayList<String> currentPlayer = new ArrayList<>(Arrays.asList(allPlayersSplit.get(i).split("\n")));
            //Check if we found the player we want to remove and exit the loop
            if((currentPlayer.get(0)+" "+currentPlayer.get(1)).equals(fullNamePlayer)) {
                allPlayersSplit.remove(i);
                break;
            }
        }

        //Clean the file
        cleanFile("log_allPlayers.txt");
        //Write the players to the file according to the accepted sign of 'AAA'
        if(allPlayersSplit.size()>0) {
            //Go over the player except the last one
            for (int i = 0; i < allPlayersSplit.size() - 1; i++) {
                writeData("log_allPlayers.txt", allPlayersSplit.get(i));
                writeData("log_allPlayers.txt", "AAA");
            }
            //Write the last player without the sign because it is not needed
            writeData("log_allPlayers.txt", allPlayersSplit.get(allPlayersSplit.size() - 1));
        }

    }

    /**
     * Get the full name of the player
     * @param p the specific player
     * @return the player.
     */
    public static String getFullName(Player p){
        return  p.getFirstName() + " " + p.getLastName();
    }

    /**
     * This function return player on a string input
     * @param playerName the specific player
     * @return the player if found and null if not.
     */
    public static Player getPlayer(String playerName){
        for (Player p:allPlayers) {
            if(Utilities.getFullName(p).equals(playerName))
                return p;
        }
        return null;
    }

    /**
     * Function that read the "log_eleven.txt" and creates a map of position and name
     * that the key is the position of the player
     * and the value is the name of player
     * @return map <position,player>
     */
    public static HashMap readPlayersForLineUp(){
       int end;
        String strPosition,strName;
       //Create map
       HashMap<String,String> playersList=new HashMap<>();
       //Read and split the file
       ArrayList<String> list=new ArrayList<>(Arrays.asList(Utilities.readFile("log_eleven.txt").split("\n")));

       for(int i=0;i<list.size();i++)
       {
           //Use - to separate position and name
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