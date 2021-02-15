package com.example.barcelonaTeam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * This fragment is the functionality of adding a new player to the system
 */
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
        /*
        Controlling all the field on screen
         */
        firstName=view.findViewById(R.id.firstnameId);
        lastName=view.findViewById(R.id.lastnameId);
        dob=view.findViewById(R.id.dobId);
        nationalTeam=view.findViewById(R.id.nationalTeamId);
        goals=view.findViewById(R.id.goalsId);
        assists=view.findViewById(R.id.assistsId);
        saves=view.findViewById(R.id.savesId);
        agreeButton=view.findViewById(R.id.agreeButton);

        /*
        Listener of agree button
         */
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new player
                Player p=new Player();
                //Flag to check if the player is already in the system
                boolean PlayerExist=false;
                int i;
                String t_firstName=firstName.getText().toString();
                String t_lastName=lastName.getText().toString();
                String t_dob=dob.getText().toString();
                String t_nationalTeam=nationalTeam.getText().toString();
                String t_goals=goals.getText().toString();
                String t_assists=assists.getText().toString();
                String t_saves=saves.getText().toString();
                //Go over all the players in the system
                for(Player temp:Utilities.allPlayers)
                    //Check if there is a matching player by first and last name
                    if(temp.getFirstName().equals(t_firstName)&&temp.getLastName().equals(t_lastName))
                        //Set flag to true
                        PlayerExist=true;

                //Check correctness of date by checking the correct position of '-'
                if(t_dob.charAt(4)!='-'||t_dob.charAt(7)!='-')
                    //Notify user of error
                     Toast.makeText(getContext(),"Please enter valid Date",Toast.LENGTH_LONG).show();
                //If the date is correct continue with checking the correctness of the flag
                else {
                    if (PlayerExist) {
                        //Notify user in case there was a match
                        Toast.makeText(getContext(), "A player with this name already exists", Toast.LENGTH_LONG).show();
                    } else {
                        //There wasn't a problem with the date or matching player
                        //Check if every field is filled with data
                        if (t_firstName.equals("") || t_lastName.equals("") || t_dob.equals("") ||
                                t_nationalTeam.equals("") || t_goals.equals("") || t_assists.equals("")
                                || t_saves.equals("")) {
                            //Notify user in case of empty field
                            Toast.makeText(getContext(), "Please don't leave empty fields", Toast.LENGTH_LONG).show();
                        } else {
                            //There isn't any problem, we build the string to contain the data of the player
                            String newStr = t_firstName + "\n" + t_lastName + "\n"
                                    + t_dob + "\n" + t_nationalTeam + "\n" + t_goals + "\n"
                                    + t_assists + "\n" + t_saves + "\n" + "AAA";
                            //Add the data of the player to the file
                            String oldStr = Utilities.readFile("log_allPlayers.txt");
                            Utilities.cleanFile("log_allPlayers.txt");
                            Utilities.writeData("log_allPlayers.txt", newStr + oldStr);

                            //Add the player to the allPlayer arrayList.
                            p.setFirstName(firstName.getText().toString());
                            p.setLastName(lastName.getText().toString());
                            p.setSaves(saves.getText().toString());
                            p.setAssists(assists.getText().toString());
                            p.setNationalTeam(nationalTeam.getText().toString());
                            p.setBirth(dob.getText().toString());
                            p.setGoals(goals.getText().toString());
                            Utilities.allPlayers.add(p);
                            //Call popBackStack in order to return to the pitch after the change
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                }
            }
        });
    }
}
