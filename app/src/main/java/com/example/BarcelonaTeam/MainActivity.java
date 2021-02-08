package com.example.BarcelonaTeam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, PlayersAdapter.dataTransfer {
    private ImageButton GK, RB, RCB, LCB, LB, LCM, RCM, ACM, LW, RW, ST;
    private TextView GKname, RBname, RCBname, LCBname, LBname, LCMname, RCMname, ACMname, LWname, RWname, STname;
    private ArrayList<ImageButton> imageArray;
    private ArrayList<TextView> textArray;
    private HashMap<String, String> playersFromFile = new HashMap<>();
    private int playerClicked;
    private DateChangeReceiver dateBroad;
    String CHANNEL_ID  = "my_channel_01";
    int notificationId = 11;
    @Override
    protected void onPause() {
        unregisterReceiver(dateBroad);
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createNotificationChannel();
        dateBroad = new DateChangeReceiver();
        IntentFilter intent = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        registerReceiver(dateBroad, intent);


        Utilities.InitializingData(getBaseContext());
        controlAllViews();

        imageArray = new ArrayList<ImageButton>(11);
        textArray = new ArrayList<TextView>(11);

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
        if (!Utilities.readFile("log_eleven.txt").equals("")) {
            playersFromFile = Utilities.readPlayersForLineUp();
            for (int i = 0; i < imageArray.size(); i++) {
                for (Map.Entry<String, String> player : playersFromFile.entrySet()) {
                    //we check if the position in the image is equal to position in the readFile
                    if (getResources().getResourceEntryName(imageArray.get(i).getId()).equals(player.getKey())) {
                        Player p = Utilities.getPlayer(player.getValue());
                        //TODO:when we enter the app after we delete someone on the pitch it collapse
                        textArray.get(i).setText(Utilities.getFullName(p));
                        imageArray.get(i).setImageResource(Utilities.getImageIdByPlayer(p));
                        imageArray.get(i).setTag(Utilities.getFullName(p));
                        p.setPosition(getResources().getResourceEntryName(imageArray.get(i).getId()));
                    }
                }
            }
        }

        /*cookbook-Listener 2*/
        View.OnClickListener imageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get player from list according to what player was pressed on the pitch
                Player displayPlayer = null;
                if (!view.getTag().toString().equals("null")) {                                       //Check if there was a player pressed or just "?"
                    for (int i = 0; i < Utilities.allPlayers.size(); i++)                                  //Go over all the players
                        if (Utilities.getFullName(Utilities.allPlayers.get(i)).equals(view.getTag()))//Check which player has the same full name of the player you pressed on
                            displayPlayer = Utilities.allPlayers.get(i);                              //Assign the player in order to send it to the fragment
                }
                //Creation and switching of the details fragment
                PlayerDetailsFrag newFrag = new PlayerDetailsFrag(displayPlayer);
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
        MenuInflater tempInflater = getMenuInflater();
        tempInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.exit:
                FragmentManager exitFragmentDialog = getSupportFragmentManager(); // the fragment for the exit dialog
                new ExitDialog().show(exitFragmentDialog, "Exit");
                break;

            case R.id.clean:
                Utilities.cleanFile("log_allPlayers.txt");
                Utilities.cleanFile("log_eleven.txt");
                break;

            case R.id.notification:
                if (item.isChecked()) {
                    item.setChecked(false);
                    Toast.makeText(this, "Notification has been disabled", Toast.LENGTH_LONG).show();
                } else {
                    item.setChecked(true);
                    Toast.makeText(this, "Notification has been enabled", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.newGames:
                NextGamesFrag newFrag1 = new NextGamesFrag();
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, newFrag1).//add on top of the static fragment
                        addToBackStack(null).//cause the back button scrolling through the loaded fragments
                        commit();
                getSupportFragmentManager().executePendingTransactions();
                break;

            case R.id.addplayer:
                AddPlayerFrag newFrag2 = new AddPlayerFrag();
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, newFrag2).//add on top of the static fragment
                        addToBackStack(null).//cause the back button scrolling through the loaded fragments
                        commit();
                getSupportFragmentManager().executePendingTransactions();
                break;

            case R.id.removePlayer:
                PlayerListFrag newFrag3 = new PlayerListFrag(R.id.removePlayer);
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, newFrag3).//add on top of the static fragment
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
    private void controlAllViews() {
        //identify the buttons
        GK = findViewById(R.id.GK);
        RB = findViewById(R.id.RB);
        RCB = findViewById(R.id.RCB);
        LCB = findViewById(R.id.LCB);
        LB = findViewById(R.id.LB);
        RCM = findViewById(R.id.RCM);
        LCM = findViewById(R.id.LCM);
        ACM = findViewById(R.id.ACM);
        LW = findViewById(R.id.LW);
        RW = findViewById(R.id.RW);
        ST = findViewById(R.id.ST);

        //identify the Text View
        GKname = findViewById(R.id.GK_TEXT);
        RBname = findViewById(R.id.RB_TEXT);
        RCBname = findViewById(R.id.RCB_TEXT);
        LCBname = findViewById(R.id.LCB_TEXT);
        LBname = findViewById(R.id.LB_TEXT);
        RCMname = findViewById(R.id.RCM_TEXT);
        LCMname = findViewById(R.id.LCM_TEXT);
        ACMname = findViewById(R.id.ACM_TEXT);
        LWname = findViewById(R.id.LW_TEXT);
        RWname = findViewById(R.id.RW_TEXT);
        STname = findViewById(R.id.ST_TEXT);
    }

    /**
     * This func set all the image button Listeners.
     *
     * @param myListener - The Listener.
     */
    private void setAllClickListeners(View.OnClickListener myListener) {
        for (ImageButton i : imageArray) {
            i.setOnLongClickListener(this);
            i.setOnClickListener(myListener);
        }
    }

    /**
     * cookbook-Listener 4
     *
     * @param view
     * @return boolean
     */
    @Override
    public boolean onLongClick(View view) {

        for (int i = 0; i < imageArray.size(); i++)
            if (imageArray.get(i) == view) {
                playerClicked = i;
            }

        PlayerListFrag newFrag = new PlayerListFrag(view.getId());
        getSupportFragmentManager().beginTransaction().
                add(R.id.pitch_main, newFrag).//add on top of the static fragment
                addToBackStack(null).//cause the back button scrolling through the loaded fragments
                commit();
        getSupportFragmentManager().executePendingTransactions();
        return true;
    }

    /**
     * implement interface of PlayersAdapter.dataTransfer to pass details of the player from here.
     *
     * @param p the specific player
     */
    @Override
    public void displayDetailPlayer(Player p) {
        for (int i = 0; i < imageArray.size(); i++) {
            if (playerClicked == i) {
                //Check if the clicked image was an existing player and remove it from the lineup
                if (!imageArray.get(i).getTag().toString().equals("null"))
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

    @Override
    public void removePlayerFromPitch(Player p) {
        int i;

        String pos = Utilities.findPositionInPitch(p);//TODO: we have setPosition but it wont work.

        for (i = 0; i < imageArray.size(); i++) {

            if ((getResources().getResourceEntryName(imageArray.get(i).getId())).equals(pos)) {
                textArray.get(i).setText(getString(R.string.None));
                //TODO:CHECK IF THIS FUNCTION OK
                imageArray.get(i).setImageResource(R.drawable.player_defulat);
                imageArray.get(i).setTag("null");
                return;
            }
        }
    }

    private void createNotificationChannel() {

        // the NotificationChannel class is new and not in the support library
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Barca_notification_memo", importance);

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * This the class of the broadCastReceiver
     */
    public class DateChangeReceiver extends BroadcastReceiver {
        //part of code that sleep and when an event is append his start to work.
        String home,away;
        LocalTime time;

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
            LocalDate thisDate=LocalDate.now();

            for(Game match:Utilities.matches)
            {
                if(match.getDate().equals(thisDate))
                {
                    home=match.getHome();
                    away=match.getAway();
                    time=match.getTime();
                }
            }

//TODO:NOT REMOVED
            try {
                Thread.sleep(43200000); //this waits a 12 hours.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Create an explicit intent for an Activity in your app
            Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);

            NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext(),CHANNEL_ID)
                    .setSmallIcon(R.drawable.barca)
                    .setContentTitle("Barca team")
                    .setContentText("Today there is a game at: "+time.toString()+" "+home+" Vs. "+ away)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationId, builder.build());
        }
    }
}

