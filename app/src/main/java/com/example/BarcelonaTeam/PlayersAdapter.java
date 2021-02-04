package com.example.BarcelonaTeam;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.MyViewHolder> implements SubstitueDialog.DialogAdapterInterface {

    private ArrayList<Player> playersList;
    private Context context;
    private dataTransfer listener;
    private Map<String,String> lineUp;
    private static String file_path;
    private ArrayList<Player> player2display;

    private PlayersAdapter adapterInstance; //We need access to the adapter that implements the subDialog

    public PlayersAdapter(Context context, dataTransfer listener) {

        this.listener =listener;
        this.context=context;
        adapterInstance =this;

        //take the data from the dada set.
        playersList=PlayersXMLParser.parsePlayers(context);
        //The line up which is saved to the file and excluded from the list of changes
        lineUp = Utilities.readPlayers();
        player2display=new ArrayList<>();
        if(lineUp!=null) {
            for (Player p : playersList)
                //all the players without the line up players.
                if (!lineUp.containsValue(Utilities.getFullName(p)))
                    player2display.add(p);
        }
        else{
            for (Player p : playersList)
                player2display.add(p);
        }
    }


    //inner class
    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemPlayerImage;
        private TextView itemFirstName, itemLastName;
        private View itemView;

        public MyViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.itemView=itemView;

            //connect the variables to there widgets in the player_detail_recycler.
            itemPlayerImage =(ImageView)itemView.findViewById(R.id.imagePlayer);
            itemFirstName =(TextView)itemView.findViewById(R.id.firstName);
            itemLastName =(TextView)itemView.findViewById(R.id.lastName);
        }

        //when we click on the item in the recyclerView
        public void bindData(Player player){
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    SubstitueDialog temp=new SubstitueDialog(player,getAdapterPosition(), adapterInstance);
                    temp.show(manager,"Substitute");
                }
            });
            //set the details in the player_item in the list
            itemFirstName.setText(player.getFirstName());
            itemLastName.setText(player.getLastName());
            Context context = itemPlayerImage.getContext();
            itemPlayerImage.setImageResource(Utilities.getImageIdByPlayer(player));
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
    }

    @Override
    public void changePlayer(Player player, int adapterPos){
        //Get the player data of the player pressed
        Player p = player2display.get(adapterPos);
        //Set the image in the player data
        p.setImgPlayer(Utilities.getImageIdByPlayer(player));
        //Call the function to display the data in the pitch
        listener.displayDetailPlayer(p);
        //add the position of the player in the pitch to the write and add func that extract the id and the name to a map.
        //Write to file the player in the line up and his position
        Utilities.writeData("log_eleven.txt",p.getPosition()+"-"+Utilities.getFullName(p));
        //Call popBackStack in order to return to the pitch and view the changes
        ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
    }
}