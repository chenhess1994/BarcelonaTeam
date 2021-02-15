package com.example.barcelonaTeam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class if for our adatper to display the list of players
 * RecyclerView as required in guidelines
 */
public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.MyViewHolder> implements SubstituteDialog.DialogAdapterInterface {

    private ArrayList<Player> playersList;
    private Context context;
    private dataTransfer listener;
    private Map<String,String> lineUp;
    private static String file_path;
    private ArrayList<Player> player2display;
    private int id;
    private PlayersAdapter adapterInstance; //We need access to the adapter that implements the subDialog

    /**
     * This constructor is used to initialize our variables and build the list of players we wish to display
     * @param context
     * @param listener
     * @param id
     */
    public PlayersAdapter(Context context, dataTransfer listener,int id) {

        this.listener =listener;
        this.context=context;
        this.adapterInstance =this;
        this.id=id;
        //Take the data of the player from the file that saves their data (filename="log_allPlayers")
        playersList=Utilities.parsePlayers();

        //The line up which is read from the file and excluded from the list of changes (filename="log_eleven")
        lineUp = Utilities.readPlayersForLineUp();

        //A list of player that changes according to what was pressed and the lineup
        player2display=new ArrayList<>();

        if(player2display.size()==0) {
            //Check if the button that was pressed was delete or change player
            if (id != R.id.removePlayer) {
                //In case change player
                if (lineUp != null) {   //Check if the lineup contains data
                    for (Player p : playersList)    //Go over all the players and exclude the player which are in the lineup
                        //check if the lineup contains the player and add it to the list to display in case it doesn't
                        if (!lineUp.containsValue(Utilities.getFullName(p)))
                            player2display.add(p);
                } else {
                    //In case the lineup is empty display all the players
                    player2display=playersList;
                }
            } else {
                //In case delete player we need to display all the players
                player2display=playersList;
            }
        }
    }

    @Override
    public PlayersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        // Inflate the custom layout (player_item).
        View itemPlayerView=inflater.inflate(R.layout.player_item, parent,false);

        // Return a new holder instance
        MyViewHolder viewHolder=new MyViewHolder(itemPlayerView);
        return viewHolder;
    }

    //Inner Class MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemPlayerImage;
        private TextView itemFirstName, itemLastName;
        private View itemView;
        private ImageView removePlayers;

        public MyViewHolder(View itemView) {
            /*  Stores the itemView in a public final member variable that can be used
                to access the context from any ViewHolder instance
                */
            super(itemView);
            this.itemView=itemView;

            //Control the fields in the app per item
            itemPlayerImage =(ImageView)itemView.findViewById(R.id.imagePlayer);
            itemFirstName =(TextView)itemView.findViewById(R.id.firstName);
            itemLastName =(TextView)itemView.findViewById(R.id.lastName);
            removePlayers=(ImageView)itemView.findViewById(R.id.removePlayerButton);

            //Display the button to delete a player according to what button was pressed to display the players
            if(id!=R.id.removePlayer)
                removePlayers.setVisibility(View.INVISIBLE);
        }

        //This functions connects between the data to display and the item in the list
        public void bindData(Player player){
            /*
            Set listener to every item
             */
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    /*
                    Display dialog to change player or disable the ability to click on the item in case you want to delete
                     */
                    if(id!=R.id.removePlayer) {
                        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                        SubstituteDialog temp = new SubstituteDialog(player, getAdapterPosition(), adapterInstance);
                        temp.show(manager, "Substitute");
                    }
                    else
                        v.setEnabled(false);
                }
            });

            //Display the details on screen from the Player object
            itemFirstName.setText(player.getFirstName());
            itemLastName.setText(player.getLastName());
            Context context = itemPlayerImage.getContext();
            /*
            If the image of the player object is set to something other than default take the correct image from the system
            Otherwise display the default image for players
             */
            if(player.getImgPlayer()!=R.drawable.player_defulat)
                itemPlayerImage.setImageResource(Utilities.getImageIdByPlayer(player));
            else
                itemPlayerImage.setImageResource(R.drawable.player_defulat);
            /*
            Set listener for the delete button
             */
            removePlayers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Delete the player from the system

                    //Delete the player from the players file
                    Utilities.removePlayerFromAllplayers(Utilities.getFullName(player));
                    //Re-read the file and change the list of player to display
                    playersList=Utilities.parsePlayers();
                    player2display.clear();
                    player2display=playersList;
                    //Notify about the removed player
                    notifyItemRemoved(getAdapterPosition());
                    //Check if the player was in the lineUp and delete from the pitch
                    if(lineUp!=null && lineUp.containsValue(Utilities.getFullName(player))) {
                        //Delete the player from the pitch
                        listener.removePlayerFromPitch(player);
                        //Delete the player also from the lineup file
                        Utilities.removePlayerFromLogLineUp(Utilities.getFullName(player));
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(PlayersAdapter.MyViewHolder holder, int position) {

        // Get the data model based on position
        Player player= player2display.get(position);

        // Set item views based on your views and data model
        holder.bindData(player);
    }


    @Override
    public int getItemCount() {
        return player2display.size();
    }



    /**
     * Implement of SubstituteDialog Interface in order to change player after we press yes.
     * @param player Player to take data from
     * @param adapterPos Position of player in list
     */
    @Override
    public void changePlayer(Player player, int adapterPos){
        //Get the player data of the player pressed
        Player p = player2display.get(adapterPos);
        //Set the image in the player data
        p=Utilities.getPlayer(Utilities.getFullName(p));
        p.setImgPlayer(Utilities.getImageIdByPlayer(player));
        //Call the function to display the data in the pitch (found in the MainActivity.)
        listener.displayDetailPlayer(p);

        //Write to file the player in the line up and his position
        Utilities.writeData("log_eleven.txt",p.getPosition()+"-"+Utilities.getFullName(p));
        //Call popBackStack in order to return to the previous screen after the change
        ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
    }
    /**
     *This interface is our way of communication between our adapter and the main activity which is the pitch
     */
    public interface dataTransfer {
        /*
        This function takes a player and display it on the pitch according to what was pressed in the adapter
         */
        public void displayDetailPlayer(Player p);
        /*
        This function takes a player and remove it from the pitch according to what was pressed in the adapter
         */
        public void removePlayerFromPitch(Player p);
    }
}

