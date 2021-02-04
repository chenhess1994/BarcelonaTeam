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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Utilities {
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
        return id;
    }

    /**
     * return the current context
     * @param context
     */
    public static void setContext(Context context){
        context_this=context;
        file_path=context.getFilesDir().getAbsolutePath();
        allPlayers=PlayersXMLParser.parsePlayers(context);
        /*The String that we load to the inside file of the system*/
        //AAA separated between games
        String games="REAL MADRID\n"   +  "FC BARCELONA\n"   +  "2021-02-03\n" + "22:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "GRANADA CF\n"     +  "2021-02-04\n" + "17:00:00\n" + "AAA" +
                     "REAL BETIS\n"    +  "FC BARCELONA\n"   +  "2021-02-05\n" + "15:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "Cádiz\n"          +  "2021-02-21\n" + "20:00:00\n" + "AAA" +
                     "Alavés\n"        +  "FC BARCELONA\n"   +  "2021-03-02\n" + "19:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "Elche\n"          +  "2021-03-05\n" + "20:00:00\n" + "AAA" +
                     "Sevilla\n"       +  "FC BARCELONA\n"   +  "2021-03-09\n" + "15:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "Osasuna\n"        +  "2021-03-20\n" + "20:00:00\n" + "AAA" +
                     "Real Sociedad\n" +  "FC BARCELONA\n"   +  "2021-04-02\n" + "20:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "Villarreal\n"     +  "2021-04-12\n" + "19:00:00\n" + "AAA" +
                     "Getafe\n"        +  "FC BARCELONA\n"   +  "2021-04-21\n" + "20:00:00";

        cleanFile("log_matches.txt");
        writeData("log_matches.txt",games);
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
     * write the data to the lof file in this way position-full name example: GK-mark ter stegen
     * @param data the data is insert (position-name)
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
            outputWriter.write(data+"\n");
            outputWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * clean the log file
     */
    public static void cleanFile(String FILE_NAME){

        //sheared Preference delete file
        //context_this.getSharedPreferences("logSP",Context.MODE_PRIVATE).edit().clear().commit();

        try {
            FileOutputStream writer = new FileOutputStream(file_path+File.separator+FILE_NAME);
            writer.write(("").getBytes());
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * remove player from log
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
    public static HashMap readPlayers(){
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

    public static void readMatches()
    {
        matches=new ArrayList<>();
        String preEditMatches=Utilities.readFile("log_matches.txt");
        ArrayList<String> allMatchesSplit = new ArrayList<>(Arrays.asList(preEditMatches.split("AAA")));
        for(String match:allMatchesSplit){
            ArrayList<String> currentGame=new ArrayList<>(Arrays.asList(match.split("\n")));
            Game g=new Game();
            g.setHome(currentGame.get(0));
            g.setAway(currentGame.get(1));
            g.setDate(LocalDate.parse(currentGame.get(2)));
            g.setTime(LocalTime.parse(currentGame.get(3)));
            //g.setResult(temp.get(4));
            matches.add(g);
        }
    }
}
