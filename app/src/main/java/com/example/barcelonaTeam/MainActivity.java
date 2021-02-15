package com.example.barcelonaTeam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class of the app display the pitch
 * Implemetns the interfacee of the adapter
 */
public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, PlayersAdapter.dataTransfer {
    private ImageButton GK, RB, RCB, LCB, LB, LCM, RCM, ACM, LW, RW, ST;
    private TextView GKname, RBname, RCBname, LCBname, LBname, LCMname, RCMname, ACMname, LWname, RWname, STname;
    private ArrayList<ImageButton> imageArray;
    private ArrayList<TextView> textArray;
    private HashMap<String, String> playersFromFile = new HashMap<>();
    private int playerClicked,notificationId = 11;
    private DateChangeReceiver dateBroad;
    private String CHANNEL_ID  = "my_channel_01";
    private Intent intentForeground ;
    private IntentFilter IntentFilter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize the data of the system
        Utilities.InitializingData(getBaseContext());
        //Take control of all the widgets on screen
        controlAllViews();
        //Create a notification channel
        createNotificationChannel();
        //Create the broadcast receiver
        dateBroad = new DateChangeReceiver();
        //Create intent to use in receiver
        IntentFilter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        //Initializing of SP
        sharedPreferences = getSharedPreferences("logSP", Context.MODE_PRIVATE);
        //Create intent to us with ForeGround Service
        intentForeground = new Intent(getBaseContext(), GameForegroundService.class);


        //Long Save RAW File
        //Check if there is data in the "log_eleven.txt"
        if (!Utilities.readFile("log_eleven.txt").equals("")) {
            //Read data from file as map
            playersFromFile = Utilities.readPlayersForLineUp();
            //Go over the array and put image and text according to the data
            for (int i = 0; i < imageArray.size(); i++) {

                for (Map.Entry<String, String> playerIterator : playersFromFile.entrySet()) {
                    //We check if the position in the image is equal to position in the readFile
                    if (getResources().getResourceEntryName(imageArray.get(i).getId()).equals(playerIterator.getKey())) {
                        Player p = Utilities.getPlayer(playerIterator.getValue());
                        textArray.get(i).setText(Utilities.getFullName(p));
                        imageArray.get(i).setImageResource(Utilities.getImageIdByPlayer(p));
                        imageArray.get(i).setTag(Utilities.getFullName(p));
                        p.setPosition(getResources().getResourceEntryName(imageArray.get(i).getId()));
                    }
                }
            }
        }

        /**cookbook-Listener 2
        Generic listener to put to all the imageButtons
         **/
        View.OnClickListener imageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get player from list according to what player was pressed on the pitch
                Player displayPlayer = null;
                if (!view.getTag().toString().equals("null")) {                                       //Check if there was a player pressed or just "?"
                    for (int i = 0; i < Utilities.allPlayers.size(); i++)                             //Go over all the players
                        if (Utilities.getFullName(Utilities.allPlayers.get(i)).equals(view.getTag())) //Check which player has the same full name of the player you pressed on
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
        //Set the above listener to all the imageButtons
        setAllClickListeners(imageListener);

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

        //Create 2 arrays to hold all the widgets of the screen
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

    /*
    Function to create notification channel
     */
    private void createNotificationChannel() {

        // the NotificationChannel class is new and not in the support library
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Barca_notification_memo", importance);

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /*
   Function to check if a service is running
    */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This the class of the broadCastReceiver
     * Which listens to the date changes
     * Broadcast receiver as required in guidelines
     */
    public class DateChangeReceiver extends BroadcastReceiver {
        //part of code that sleep and when an event is append his start to work.
        String home,away;
        LocalTime time;

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
            //Get current date
            LocalDate thisDate=LocalDate.now();
            //Go over all the matches
            for(Game match:Utilities.matches)
            {
                //Check if the match is today and get the data
                if(match.getDate().equals(thisDate))
                {
                    home=match.getHome();
                    away=match.getAway();
                    time=match.getTime();
                }
            }
            /*
            The onReceive occurs on 00:00 because it listens to ACTION_DATE_CHANGED
            We want that the app will notify you on 12:00 so we wait 43200000 millis
             */
            try {
                Thread.sleep(43200000); //this waits a 12 hours.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Create an explicit intent for an Activity in your app
            Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
            //Creation of notification
            NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext(),CHANNEL_ID)
                    .setSmallIcon(R.mipmap.barca)
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

    @Override
    protected void onPause() {
        //Unregister broadcast receiver
        unregisterReceiver(dateBroad);
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Register broadcast receiver
        registerReceiver(dateBroad, IntentFilter);
        super.onResume();
    }

    /**
     * Menu as required in guidelines
     * @param menu
     * @return
     */
    /*
    Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater tempInflater = getMenuInflater();
        tempInflater.inflate(R.menu.main_menu, menu);
        /**
         * SP long range saving
         */
        //Get the state of the notification checkbox from the SP
        menu.getItem(2).setChecked(sharedPreferences.getBoolean("notification_state",false));
        //Set the flag for the ForeGround service according to the state
        Utilities.flagForegroundNotification=menu.getItem(2).isChecked();
        //Check if the service is running and the state of the checkbox
        if (!isMyServiceRunning(GameForegroundService.class)&&Utilities.flagForegroundNotification) {
            //In case both the state is checked and the service isn't running start the service again
            getBaseContext().startForegroundService(intentForeground);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /*
    Set the action to do when pressing on the options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Do a proper exit from the app
            case R.id.exit:
                FragmentManager exitFragmentDialog = getSupportFragmentManager(); // the fragment for the exit dialog
                new ExitDialog().show(exitFragmentDialog, "Exit");
                break;

            case R.id.notification:
                //Check state of checkbox
                if (item.isChecked()) {//In case it was checked
                    //Set it to unchecked
                    item.setChecked(false);
                    //Display Toast of disabled notification
                    Toast.makeText(this, "Notification has been disabled", Toast.LENGTH_LONG).show();
                    //Stop the ForeGround service
                    stopService(intentForeground);
                    //Set the flag for ForeGround service as false
                    Utilities.flagForegroundNotification=false;
                } else {//In case it was unchecked
                    //Set it to checked
                    item.setChecked(true);
                    //Display Toast of enabled notification
                    Toast.makeText(this, "Notification has been enabled", Toast.LENGTH_LONG).show();
                    //Set the flag for ForeGround service as true
                    Utilities.flagForegroundNotification=true;
                    //Check if the service is running
                    if (!isMyServiceRunning(GameForegroundService.class))
                        //Start it in case it isn't
                        startForegroundService(intentForeground);
                }
                //Create editor of SP
                SharedPreferences.Editor editor=sharedPreferences.edit();
                //Save the state of the checkbox in SP
                editor.putBoolean("notification_state",item.isChecked());
                //Save changes to SP
                editor.apply();
                break;
            //Create and display the fragment of the next matches
            case R.id.newGames:
                NextGamesFrag NextGames = new NextGamesFrag();
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, NextGames).//add on top of the static fragment
                        addToBackStack(null).//cause the back button scrolling through the loaded fragments
                        commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            //Create and display the fragment of adding a player
            case R.id.addplayer:
                AddPlayerFrag AddPlayer = new AddPlayerFrag();
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, AddPlayer).//add on top of the static fragment
                        addToBackStack(null).//cause the back button scrolling through the loaded fragments
                        commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            //Create and display the fragment of removing a player
            case R.id.removePlayer:
                //Create the fragment and pass to it the id of remove player
                PlayerListFrag removePlayer = new PlayerListFrag(R.id.removePlayer);
                getSupportFragmentManager().beginTransaction().
                        add(R.id.pitch_main, removePlayer).//add on top of the static fragment
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
     * cookbook-Listener 4
     * This function is the actions to do on long click on an imageButton
     * @param view
     * @return boolean
     */
    @Override
    public boolean onLongClick(View view) {
        //Go over all the imageButtons and find the one that was clicked
        for (int i = 0; i < imageArray.size(); i++)
            if (imageArray.get(i) == view) {
                //Save the location of the imageButton in order to change it after coming back from choosing which player to change
                playerClicked = i;
            }
        //Create a fragment of list and pass it the id of the imageButton
        PlayerListFrag ChangePlayer = new PlayerListFrag(view.getId());
        getSupportFragmentManager().beginTransaction().
                add(R.id.pitch_main, ChangePlayer).//add on top of the static fragment
                addToBackStack(null).//cause the back button scrolling through the loaded fragments
                commit();
        getSupportFragmentManager().executePendingTransactions();
        return true;
    }

    /**
     * Implement interface of PlayersAdapter.dataTransfer to pass details of the player to here.
     *
     * @param p the specific player
     */
    @Override
    public void displayDetailPlayer(Player p) {
        //Go over all the imageButton to find the one that was pressed
        for (int i = 0; i < imageArray.size(); i++) {
            if (playerClicked == i) {
                //Check if the clicked image was an existing player and remove it from the lineup
                if (!imageArray.get(i).getTag().toString().equals("null"))
                    Utilities.removePlayerFromLogLineUp(imageArray.get(i).getTag().toString());

                //Set tag for which player is in the location by full name
                imageArray.get(i).setTag(Utilities.getFullName(p));
                Utilities.getPlayer(Utilities.getFullName(p)).setPosition(getResources().getResourceEntryName(imageArray.get(i).getId()));

                //Change the image and text to match the player added
                imageArray.get(i).setImageResource(p.getImgPlayer());
                textArray.get(i).setText(Utilities.getFullName(p));
                break;
            }
        }
    }

    /**
     * Implement the interface of PlayersAdapter.dataTransfer to remove player from the pitch
     * @param p specific player
     */
    @Override
    public void removePlayerFromPitch(Player p) {
        int i;
        //Get the position of the player in the pitch
        String pos =Utilities.getPlayer(Utilities.getFullName(p)).getPosition();
        //Go over all the imageButtons to find the correct one
        for (i = 0; i < imageArray.size(); i++) {
            if ((getResources().getResourceEntryName(imageArray.get(i).getId())).equals(pos)) {
                //Once found remove the details such as image and name from the pitch
                textArray.get(i).setText(getString(R.string.None));
                //Set the image to question mark
                imageArray.get(i).setImageResource(R.drawable.question_mark);
                //Set tag to null
                imageArray.get(i).setTag("null");
                return;
            }
        }
    }



}

