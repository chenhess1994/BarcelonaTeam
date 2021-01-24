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
import java.util.ArrayList;
import java.util.Arrays;

public class Utilities {
    public static final String FILE_NAME="log_eleven.txt";
    private static String file_path;
    private static Context context_this;
    public static ArrayList<Player> allPlayers;

    public static int flagForSub=0;

    public static int getImageIdByPlayer(Player player){
        String nameIdentifier = (player.getFirstName() + "_" + player.getLastName()).toLowerCase();
        int id = context_this.getResources().getIdentifier(nameIdentifier, "drawable", context_this.getPackageName());
        return id;
    }

    public static void setContext(Context context){
        context_this=context;
        file_path=context.getFilesDir().getAbsolutePath();
        allPlayers=PlayersXMLParser.parsePlayers(context);
    }

    public static String readFile() {
        StringBuilder text= null;
        String line;
        ArrayList<String> temp=new ArrayList<>();
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
            Log.i("test","errorrrrr");
            e.printStackTrace();
        }

        return text.toString();
    }

    public static void writeData(String data) {
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

    public static void cleanFile(){
        try {
            FileOutputStream writer = new FileOutputStream(file_path+File.separator+FILE_NAME);
            writer.write(("").getBytes());
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void removePlayerFromLogLineUp(String fullNamePlayer){
        ArrayList<String> lineUpTemp = new ArrayList<String>(Arrays.asList(Utilities.readFile().split("\n")));
        lineUpTemp.remove(fullNamePlayer);
        cleanFile();
        for (String str: lineUpTemp) {
            writeData(str);
        }
    }

    public static String getFullName(Player p){
        return  p.getFirstName() + " " + p.getLastName();
    }
}








//    public static BarDialog newInstance(int progressBar) {
//        BarDialog f = new BarDialog();
//        // Supply progressBar input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("progressBar", progressBar);
//        f.setArguments(args);
//        return f;
//    }



