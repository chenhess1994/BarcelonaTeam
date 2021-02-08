package com.example.BarcelonaTeam;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerListFrag extends Fragment {
    private RecyclerView rvPlayer;
    private PlayersAdapter playerAdapter;
    private PlayersAdapter.dataTransfer listener;
    private int id;
    private TextView change_delete_tv;

    public PlayerListFrag(int id) {
        this.id=id;
    }

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
        change_delete_tv=view.findViewById(R.id.change_delete_tv);
        if(id==R.id.removePlayer)
            change_delete_tv.setText(getResources().getText(R.string.delete_player));
        else
            change_delete_tv.setText(getResources().getText(R.string.Change_player));

        rvPlayer=(RecyclerView) view.findViewById(R.id.recyclerView);

        //creating adapter
        playerAdapter =new PlayersAdapter(this.getContext(), listener,id);

        // Set layout manager to position the items
        rvPlayer.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Attach the adapter to the recyclerview to populate items
        rvPlayer.setAdapter(playerAdapter);
    }

}
