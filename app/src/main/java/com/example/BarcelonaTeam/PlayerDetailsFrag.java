package com.example.BarcelonaTeam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PlayerDetailsFrag extends Fragment {
    private Player displayPlayer;
    private TextView name,DOB,national,goals,assist,saves;
    private ImageView playerImage;
    public PlayerDetailsFrag(Player p){
        displayPlayer=p;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_details,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name=view.findViewById(R.id.Details_Name);
        DOB=view.findViewById(R.id.Details_DOB);
        national=view.findViewById(R.id.Details_National);
        goals=view.findViewById(R.id.Details_Goals);
        assist=view.findViewById(R.id.Details_Assists);
        saves=view.findViewById(R.id.Details_Saves);
        playerImage=view.findViewById(R.id.playerDetailsImage);
        if(displayPlayer!=null) {
            playerImage.setImageResource(Utilities.getImageIdByPlayer(displayPlayer));
            name.setText(Utilities.getFullName(displayPlayer));
            DOB.setText(displayPlayer.getBirth());
            national.setText(displayPlayer.getNationalTeam());
            goals.setText(displayPlayer.getGoals());
            assist.setText(displayPlayer.getAssists());
            saves.setText(displayPlayer.getSaves());
        }
        else
            Log.i("yuda","Player null Details");

    }
}
