package com.example.BarcelonaTeam;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerListFrag extends Fragment {
    private RecyclerView rvPlayer;
    private PlayersAdapter playerAdapter;
    private PlayersAdapter.dataTransfer listener;
    @Override
    public void onAttach(Context context) {
        try{
            listener = (PlayersAdapter.dataTransfer) context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " + getActivity().getClass().getName() + " must implements the interface 'templ'");
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_lists_players,container,false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlayer=(RecyclerView) view.findViewById(R.id.recyclerView);

        //creating adapter
        playerAdapter =new PlayersAdapter(this.getContext(), listener);

        // Set layout manager to position the items
        rvPlayer.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Attach the adapter to the recyclerview to populate items
        rvPlayer.setAdapter(playerAdapter);
    }

}
