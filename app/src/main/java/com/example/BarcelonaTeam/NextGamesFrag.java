package com.example.BarcelonaTeam;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.LocalTime;

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


        for(index=0; index<Utilities.matches.size();index++) {
            Game match=Utilities.matches.get(index);

            if (dateNow.isBefore(match.getDate())) break;
            if (dateNow.isEqual(match.getDate()) && timeNow.isBefore(match.getTime())) break;
        }

        /*put the match in his place in the layout*/
        putMatch(index,match1,homeId1,awayId1,dateId1,timeId1);
        putMatch(index+1,match2,homeId2,awayId2,dateId2,timeId2);
        putMatch(index+2,match3,homeId3,awayId3,dateId3,timeId3);
    }

    private void putMatch(int position,Game match,TextView home,TextView away,TextView date,TextView time){
        if(position<Utilities.matches.size()) {
            match=Utilities.matches.get(position);
            date.setText(match.getDate().toString());
            time.setText(match.getTime().toString());
            home.setText(match.getHome());
            away.setText(match.getAway());
        }
    }
}
