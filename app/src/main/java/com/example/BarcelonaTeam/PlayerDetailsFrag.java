package com.example.BarcelonaTeam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PlayerDetailsFrag extends Fragment  {
    private Player displayPlayer;
    private TextView name,DOB,national,goals,assist,saves;
    private ImageView playerImage;
    private EditText commentOnPlayer;
    private SharedPreferences sharedPreferences;
    private Button commentConfirm;
    private String commentSP;


    /**
     *  In this constructor we connected this fragment to the player are display.
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

        //sheared Preference about the comment of the player in the fragment.
        sharedPreferences = getActivity().getSharedPreferences("logSP", Context.MODE_PRIVATE);

        super.onViewCreated(view, savedInstanceState);

        //Declaration
        name=view.findViewById(R.id.Details_Name);
        DOB=view.findViewById(R.id.Details_DOB);
        national=view.findViewById(R.id.Details_National);
        goals=view.findViewById(R.id.Details_Goals);
        assist=view.findViewById(R.id.Details_Assists);
        saves=view.findViewById(R.id.Details_Saves);
        playerImage=view.findViewById(R.id.playerDetailsImage);
        commentOnPlayer=view.findViewById(R.id.commentPlayer);
        commentConfirm=view.findViewById(R.id.confirmComment);

        if(displayPlayer!=null) {
            //sheared Preference about the comment of the player in the fragment.
            if(sharedPreferences.contains(Utilities.getFullName(displayPlayer))) {
                commentSP=sharedPreferences.getString(Utilities.getFullName(displayPlayer), "Not found");
                commentOnPlayer.setText(commentSP);
            }

            playerImage.setImageResource(Utilities.getImageIdByPlayer(displayPlayer));
            name.setText(Utilities.getFullName(displayPlayer));
            DOB.setText(displayPlayer.getBirth());
            national.setText(displayPlayer.getNationalTeam());
            goals.setText(displayPlayer.getGoals());
            assist.setText(displayPlayer.getAssists());
            saves.setText(displayPlayer.getSaves());
            commentOnPlayer.setEnabled(true);
        }
        else {
            commentOnPlayer.setEnabled(false);
            commentConfirm.setEnabled(false);
        }


        //in this listener we save the comment to our SP file
        commentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //now we can edit something in our file of SP
                SharedPreferences.Editor editor=sharedPreferences.edit();
                //here we insert to the SP file the deatils of our player comment.
                editor.putString(Utilities.getFullName(displayPlayer),commentOnPlayer.getText().toString());
                //to close the process like transaction we close it with apply
                editor.apply();
            }
        });
    }

}
