package com.example.BarcelonaTeam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class AddPlayerFrag extends Fragment {
    private EditText firstName,lastName,dob,nationalTeam,goals,assists,saves;
    private ImageView playerImage;
    private Button agreeButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_player_layout,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        firstName=view.findViewById(R.id.firstnameId);
        lastName=view.findViewById(R.id.lastnameId);
        dob=view.findViewById(R.id.dobId);
        nationalTeam=view.findViewById(R.id.nationalTeamId);
        goals=view.findViewById(R.id.goalsId);
        assists=view.findViewById(R.id.assistsId);
        saves=view.findViewById(R.id.savesId);
        agreeButton=view.findViewById(R.id.agreeButton);


        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newStr =firstName.getText().toString()+"\n"
                            +lastName.getText().toString()+"\n"
                            +dob.getText().toString()+"\n"
                            +nationalTeam.getText().toString()+"\n"
                            +goals.getText().toString()+"\n"
                            +assists.getText().toString()+"\n"
                            +saves.getText().toString()+"\n"+"AAA";
                //TODO:QA on the fields
                String oldStr=Utilities.readFile("log_allPlayers.txt");
                Utilities.cleanFile("log_allPlayers.txt");
                Utilities.writeData("log_allPlayers.txt",newStr+oldStr);
                String s=Utilities.readFile("log_allPlayers.txt");
                Log.i("test",s);
                //Call popBackStack in order to return to the pitch after the change
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
