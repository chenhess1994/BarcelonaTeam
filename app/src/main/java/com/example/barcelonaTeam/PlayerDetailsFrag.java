package com.example.barcelonaTeam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * This fragment class is for displaying the details of a player in our system after he was clicked
 */
public class PlayerDetailsFrag extends Fragment  {
    private Player displayPlayer;
    private TextView name,DOB,national,goals,assist,saves;
    private ImageView playerImage;
    private EditText commentOnPlayer;
    private SharedPreferences sharedPreferences;
    private Button commentConfirm;
    private String commentSP;


    /**
     *  This constructor is for linking the player that was clicked with the created fragment
     * @param p
     */
    public PlayerDetailsFrag(Player p){
        this.displayPlayer=p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_details,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        //We use SP in order to save the personal comment on the player which the user has typed
        /**
         * SP long range saving
         */
        sharedPreferences = getActivity().getSharedPreferences("logSP", Context.MODE_PRIVATE);
        super.onViewCreated(view, savedInstanceState);

        /*
        Take control of the fields in the app
         */
        name=view.findViewById(R.id.Details_Name);
        DOB=view.findViewById(R.id.Details_DOB);
        national=view.findViewById(R.id.Details_National);
        goals=view.findViewById(R.id.Details_Goals);
        assist=view.findViewById(R.id.Details_Assists);
        saves=view.findViewById(R.id.Details_Saves);
        playerImage=view.findViewById(R.id.playerDetailsImage);
        commentOnPlayer=view.findViewById(R.id.commentPlayer);
        commentConfirm=view.findViewById(R.id.confirmComment);

        /*
        If a player with details is clicked we need to display the written comment about him and its details.
         */
        if(displayPlayer!=null) {
            //SP in order to get the written comment
            if(sharedPreferences.contains(Utilities.getFullName(displayPlayer))) {
                commentSP=sharedPreferences.getString(Utilities.getFullName(displayPlayer), "Not found");
                commentOnPlayer.setText(commentSP);
            }
            /*
            Set the details which are taken from the Player object which was sent using the builder
             */
            playerImage.setImageResource(Utilities.getImageIdByPlayer(displayPlayer));
            name.setText(Utilities.getFullName(displayPlayer));
            DOB.setText(displayPlayer.getBirth());
            national.setText(displayPlayer.getNationalTeam());
            goals.setText(displayPlayer.getGoals());
            assist.setText(displayPlayer.getAssists());
            saves.setText(displayPlayer.getSaves());
            //Make the comment section editable
            commentOnPlayer.setEnabled(true);
        }
        else {
            /*
            In case the player that was clicked wasn't and actual player we disable the comment and the "confirm" button
             */
            commentOnPlayer.setEnabled(false);
            commentConfirm.setEnabled(false);
        }


        /**
         * Setting the listener for the "confirm" button
         */
        commentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open SP editor to add our comment
                SharedPreferences.Editor editor=sharedPreferences.edit();
                //Attach our comment to the player name in the SP
                editor.putString(Utilities.getFullName(displayPlayer),commentOnPlayer.getText().toString());
                //Save changes for the editor
                editor.apply();
                //PopBackStack to return to the pitch
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}
