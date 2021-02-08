package com.example.BarcelonaTeam;

import android.content.Context;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.MyViewHolder> implements SubstitueDialog.DialogAdapterInterface {

    private ArrayList<Player> playersList;
    private Context context;
    private dataTransfer listener;
    private Map<String,String> lineUp;
    private static String file_path;
    private ArrayList<Player> player2display;
    private int id;
    private PlayersAdapter adapterInstance; //We need access to the adapter that implements the subDialog

    public PlayersAdapter(Context context, dataTransfer listener,int id) {

        this.listener =listener;
        this.context=context;
        this.adapterInstance =this;
        this.id=id;
        //take the data from the dada set.
        playersList=Utilities.parsePlayers();//yuda amar lahshov al ze
        //The line up which is saved to the file and excluded from the list of changes
        lineUp = Utilities.readPlayersForLineUp();
        player2display=new ArrayList<>();
        if(player2display.size()==0) {
            if (id != R.id.removePlayer) {
                if (lineUp != null) {
                    for (Player p : playersList)
                        //all the players without the line up players.
                        if (!lineUp.containsValue(Utilities.getFullName(p)))
                            player2display.add(p);
                } else {
                    for (Player p : playersList)
                        player2display.add(p);
                }
            } else {
                //if the id his removePlayer
                for (Player p : playersList)
                    player2display.add(p);
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

    //inner class
    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemPlayerImage;
        private TextView itemFirstName, itemLastName;
        private View itemView;
        private ImageView removePlayers;
        public MyViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.itemView=itemView;

            //connect the variables to there widgets in the player_detail_recycler.
            itemPlayerImage =(ImageView)itemView.findViewById(R.id.imagePlayer);
            itemFirstName =(TextView)itemView.findViewById(R.id.firstName);
            itemLastName =(TextView)itemView.findViewById(R.id.lastName);
            removePlayers=(ImageView)itemView.findViewById(R.id.removePlayerButton);

            //if the removePlayer id is not match to the button if yes doesn't show it in layout
            if(id!=R.id.removePlayer)
                removePlayers.setVisibility(View.INVISIBLE);
        }

        //when we click on the item in the recyclerView
        public void bindData(Player player){
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(id!=R.id.removePlayer) {
                        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                        SubstitueDialog temp = new SubstitueDialog(player, getAdapterPosition(), adapterInstance);
                        temp.show(manager, "Substitute");
                    }
                    else
                        v.setEnabled(false);
                }
            });

            //set the details in the player_item in the list
            itemFirstName.setText(player.getFirstName());
            itemLastName.setText(player.getLastName());
            Context context = itemPlayerImage.getContext();
            if(player.getImgPlayer()!=R.drawable.player_defulat)
                itemPlayerImage.setImageResource(Utilities.getImageIdByPlayer(player));
            else
                itemPlayerImage.setImageResource(R.drawable.player_defulat);

            removePlayers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //delete the player from the list in the removePlayer RecyclerView and return the update list
                    Utilities.removePlayerFromAllplayers(Utilities.getFullName(player));
                    playersList=Utilities.parsePlayers();
                    //notify about the removed.
                    notifyItemRemoved(getAdapterPosition());

                    //check if the player is in the lineUp to delete him from pitch
                    if(lineUp!=null && lineUp.containsValue(Utilities.getFullName(player))) {
                        //delete the player also from the pitch
                        listener.removePlayerFromPitch(player);
                        //delete the player also from the line up file
                        Utilities.removePlayerFromLogLineUp(Utilities.getFullName(player));
                    }

                    player2display.clear();
                    for (Player p : playersList)
                        player2display.add(p);

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


    public interface dataTransfer {
        public void displayDetailPlayer(Player p);
        public void removePlayerFromPitch(Player p);
    }

    /**
     * Implement of SubstituteDialog after we press there yes.
     * @param player
     * @param adapterPos
     */
    @Override
    public void changePlayer(Player player, int adapterPos){
        //Get the player data of the player pressed
        Player p = player2display.get(adapterPos);
        //Set the image in the player data
        p.setImgPlayer(Utilities.getImageIdByPlayer(player));//TODO:think if it necessery
        //Call the function to display the data in the pitch (found in the MainActivity.)
        listener.displayDetailPlayer(p);
        //add the position of the player in the pitch to the write and add func that extract the id and the name to a map.
        //Write to file the player in the line up and his position
        Utilities.writeData("log_eleven.txt",p.getPosition()+"-"+Utilities.getFullName(p));
        //Call popBackStack in order to return to the pitch after the change
        ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
    }
}

