package com.example.barcelonaTeam;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This fragment is the class of the functionality of watching the closest games of the team.
 */
public class NextGamesFrag extends Fragment {

    private TextView dateId1,homeId1,awayId1,timeId1;
    private TextView dateId2,homeId2,awayId2,timeId2;
    private TextView dateId3,homeId3,awayId3,timeId3;
    private int index;
    private Game match1,match2,match3;
    private LocalTime timeNow = LocalTime.now();
    private LocalDate dateNow = LocalDate.now();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nextgames,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        /*
        Take control of the fields in the app
         */
        dateId1=view.findViewById(R.id.dateId1);
        timeId1=view.findViewById(R.id.timeId1);
        homeId1=view.findViewById(R.id.homeId1);
        awayId1=view.findViewById(R.id.awayId1);

        dateId2=view.findViewById(R.id.dateId2);
        timeId2=view.findViewById(R.id.timeId2);
        homeId2=view.findViewById(R.id.homeId2);
        awayId2=view.findViewById(R.id.awayId2);

        dateId3=view.findViewById(R.id.dateId3);
        timeId3=view.findViewById(R.id.timeId3);
        homeId3=view.findViewById(R.id.homeId3);
        awayId3=view.findViewById(R.id.awayId3);

        /*
        Go over all the matches of the team and check which match is the closest and add three matches forward
         */
        for(index=0; index<Utilities.matches.size();index++) {
            Game match=Utilities.matches.get(index);
            //check if the game is at a later date
            if (dateNow.isBefore(match.getDate())) break;
            //check if the game is today at a later time
            if (dateNow.isEqual(match.getDate()) && timeNow.isBefore(match.getTime())) break;
        }
        /*put the match in his place in the layout*/
        putMatch(index,match1,homeId1,awayId1,dateId1,timeId1);
        putMatch(index+1,match2,homeId2,awayId2,dateId2,timeId2);
        putMatch(index+2,match3,homeId3,awayId3,dateId3,timeId3);
    }

    /**
     * This function take the data and display it on screen
     * @param position index of match in the array of matches
     * @param match match details
     * @param home  TextView
     * @param away  TextView
     * @param date  TextView
     * @param time  TextView
     */
    private void putMatch(int position,Game match,TextView home,TextView away,TextView date,TextView time){
        //set the details to the layout.
        if(position<Utilities.matches.size()) {
            match=Utilities.matches.get(position);
            // change the format of the date to dd/mm/yy from yyyy-mm-dd
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateFormat = match.getDate().format(pattern);
            date.setText(dateFormat);

            time.setText(match.getTime().toString());
            home.setText(match.getHome());
            away.setText(match.getAway());
        }
    }
}
