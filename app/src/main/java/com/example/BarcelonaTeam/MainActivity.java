package com.example.BarcelonaTeam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, PlayersAdapter.dataTransfer {
    private ImageButton GK,RB,RCB,LCB,LB,LCM,RCM,ACM,LW,RW,ST;
    private TextView GKname,RBname,RCBname,LCBname,LBname,LCMname,RCMname,ACMname,LWname,RWname,STname;
    private ArrayList<ImageButton> imageArray;
    private ArrayList<TextView>    textArray;
    private HashMap<String,String> playersFromFile =new HashMap<>();
    private int playerClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Utilities.setContext(getBaseContext());
        controlAllViews();
        Utilities.readMatches();

        imageArray=new ArrayList<ImageButton>(11);
        textArray=new ArrayList<TextView>(11);

        imageArray.add(GK);
        imageArray.add(RB);
        imageArray.add(RCB);
        imageArray.add(LCB);
        imageArray.add(LB);
        imageArray.add(LCM);
        imageArray.add(RCM);
        imageArray.add(ACM);
        imageArray.add(LW);
        imageArray.add(RW);
        imageArray.add(ST);

        textArray.add(GKname);
        textArray.add(RBname);
        textArray.add(RCBname);
        textArray.add(LCBname);
        textArray.add(LBname);
        textArray.add(LCMname);
        textArray.add(RCMname);
        textArray.add(ACMname);
        textArray.add(LWname);
        textArray.add(RWname);
        textArray.add(STname);

        //if the file is not empty so put the details in the pitch
        if(!Utilities.readFile("log_eleven.txt").equals(""))
        {
            playersFromFile =Utilities.readPlayers();
            for(int i=0;i<imageArray.size();i++)
            {
                for(Map.Entry<String, String> player: playersFromFile.entrySet())
                {
                    //we check if the position in the image is equal to position in the readFile
                    if(getResources().getResourceEntryName(imageArray.get(i).getId()).equals(player.getKey()))
                    {
                        Player p=Utilities.getPlayer(player.getValue());
                        textArray.get(i).setText(Utilities.getFullName(p));
                        imageArray.get(i).setImageResource(Utilities.getImageIdByPlayer(p));
                        imageArray.get(i).setTag(Utilities.getFullName(p));
                        p.setPosition(getResources().getResourceEntryName(imageArray.get(i).getId()));
                    }
                }
            }
        }

        /*cookbook-Listener 2*/
        View.OnClickListener imageListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get player from list according to what player was pressed on the pitch
                Player displayPlayer=null;
                if(!view.getTag().toString().equals("null")){                                       //Check if there was a player pressed or just "?"
                    for(int i=0;i<Utilities.allPlayers.size();i++)                                  //Go over all the players
                        if(Utilities.getFullName(Utilities.allPlayers.get(i)).equals(view.getTag()))//Check which player has the same full name of the player you pressed on
                            displayPlayer=Utilities.allPlayers.get(i);                              //Assign the player in order to send it to the fragment
                }
                //Creation and switching of the details fragment
                PlayerDetailsFrag newFrag= new PlayerDetailsFrag(displayPlayer);
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, newFrag).//add on top of the static fragment
                        addToBackStack(null).//cause the back button scrolling through the loaded fragments
                        commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        };

        setAllClickListeners(imageListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater tempInflater=getMenuInflater();
        tempInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.exit:
                FragmentManager exitFragmentDialog=getSupportFragmentManager(); // the fragment for the exit dialog
                new ExitDialog().show(exitFragmentDialog,"Exit");
                break;
            case R.id.clean:
                Utilities.cleanFile("log_eleven.txt");
                break;
            case R.id.notification:
                if(item.isChecked()) {
                    item.setChecked(false);
                    Toast.makeText(this,"Notification has been disabled",Toast.LENGTH_LONG).show();
                }
                else {
                    item.setChecked(true);
                    Toast.makeText(this,"Notification has been enabled",Toast.LENGTH_LONG).show();
                }
                //notificationItem=item;
                break;
            case R.id.newGames:
                NextGamesFrag newFrag= new NextGamesFrag();
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, newFrag).//add on top of the static fragment
                        addToBackStack(null).//cause the back button scrolling through the loaded fragments
                        commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * This func will take control over all the textViews and ImageButtons on the pitch
     */
    private void controlAllViews(){
        //identify the buttons
        GK=findViewById(R.id.GK);
        RB=findViewById(R.id.RB);
        RCB=findViewById(R.id.RCB);
        LCB=findViewById(R.id.LCB);
        LB=findViewById(R.id.LB);
        RCM=findViewById(R.id.RCM);
        LCM=findViewById(R.id.LCM);
        ACM=findViewById(R.id.ACM);
        LW=findViewById(R.id.LW);
        RW=findViewById(R.id.RW);
        ST=findViewById(R.id.ST);

        //identify the Text View
        GKname=findViewById(R.id.GK_TEXT);
        RBname=findViewById(R.id.RB_TEXT);
        RCBname=findViewById(R.id.RCB_TEXT);
        LCBname=findViewById(R.id.LCB_TEXT);
        LBname=findViewById(R.id.LB_TEXT);
        RCMname=findViewById(R.id.RCM_TEXT);
        LCMname=findViewById(R.id.LCM_TEXT);
        ACMname=findViewById(R.id.ACM_TEXT);
        LWname=findViewById(R.id.LW_TEXT);
        RWname=findViewById(R.id.RW_TEXT);
        STname=findViewById(R.id.ST_TEXT);
    }
    /**
     * This func set all the image button Listeners.
     * @param myListener - The Listener.
     */
    private void setAllClickListeners(View.OnClickListener myListener){
        for(ImageButton i:imageArray) {
            i.setOnLongClickListener(this);
            i.setOnClickListener(myListener);
        }
    }

    /**
     * cookbook-Listener 4
     * @param view
     * @return boolean
     */
    @Override
    public boolean onLongClick(View view) {

        for(int i=0;i<imageArray.size();i++)
            if(imageArray.get(i)==view){
                playerClicked=i;
            }

        PlayerListFrag newFrag= new PlayerListFrag();
        getSupportFragmentManager().beginTransaction().
                add(R.id.pitch_main, newFrag).//add on top of the static fragment
                addToBackStack(null).//cause the back button scrolling through the loaded fragments
                commit();
        getSupportFragmentManager().executePendingTransactions();
        return true;
    }

    /**
     * implement interface of PlayersAdapter.dataTransfer to pass details of the player from here.
     * @param p the specific player
     */
    @Override
    public void displayDetailPlayer(Player p) {
        for(int i=0;i<imageArray.size();i++) {
            if(playerClicked==i){
                //Check if the clicked image was an existing player and remove it from the lineup
                if(!imageArray.get(i).getTag().toString().equals("null"))
                    Utilities.removePlayerFromLogLineUp(imageArray.get(i).getTag().toString());

                //Set tag for which player is in the location by full name
                imageArray.get(i).setTag(Utilities.getFullName(p));
                p.setPosition(getResources().getResourceEntryName(imageArray.get(i).getId()));

                //Change the image and text to match the player added
                imageArray.get(i).setImageResource(p.getImgPlayer());
                textArray.get(i).setText(Utilities.getFullName(p));
                break;
            }
        }
    }
}

/*
* //AAA separated between games
        String games="FC BARCELONA\n"  +  "REAL MADRID\n"   +  "2021-02-03\n" + "22:00:00\n" + "AAA" +
                     "GRANADA CF\n"    +  "FC BARCELONA\n"  +  "2021-02-04\n" + "17:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "REAL BETIS\n"    +  "2021-02-05\n" + "15:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "Cádiz\n"         +  "2021-02-06\n" + "20:00:00\n" + "AAA" +
                     "Elche\n"         +  "FC BARCELONA\n"  +  "2021-03-13\n" + "20:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "ALAVES\n"        +  "2021-03-20\n" + "20:00:00\n" + "AAA" +
                     "Sevilla\n\n"     +  "FC BARCELONA\n"  +  "2021-03-25\n" + "22:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "ALAVES\n"        +  "2021-04-02\n" + "22:00:00\n" + "AAA" +
                     "Osasuna\n"       +  "FC BARCELONA\n"  +  "2021-05-21\n" + "22:00:00\n" + "AAA" +
                     "FC BARCELONA\n"  +  "Real Sociedad\n" +  "2021-06-20\n" + "22:00:00\n";*/