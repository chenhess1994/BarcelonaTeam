package com.example.barcelonaTeam;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * This fragment class is the use of recyclerView in our system
 * We use it to display our list of players
 * RecyclerView as required in guidelines
 */
public class PlayerListFrag extends Fragment {
    private RecyclerView rvPlayer;
    private PlayersAdapter playerAdapter;
    private PlayersAdapter.dataTransfer listener;
    private int id;
    private TextView change_delete_tv;
    /*
    This constructor is given an id parameter in order to distinguish between Changing player and deleting one
     */
    public PlayerListFrag(int id) {
        this.id=id;
    }

    @Override
    public void onAttach(Context context) {
        /*
        We use an interface and listener object in order to communicate between our main activity and our adapter
         */
        try{
            listener = (PlayersAdapter.dataTransfer) context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " + getActivity().getClass().getName() + " must implements the interface 'dataTransfer'");
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
        /*
        We change the headline according to what button was clicked
         */
        change_delete_tv=view.findViewById(R.id.change_delete_tv);
        if(id==R.id.removePlayer)
            change_delete_tv.setText(getResources().getText(R.string.delete_player));
        else
            change_delete_tv.setText(getResources().getText(R.string.Change_player));
        //Taking control of the RecyclerView
        rvPlayer=(RecyclerView) view.findViewById(R.id.recyclerView);

        //Creating our adapter and passing to it our Listener and id
        playerAdapter =new PlayersAdapter(this.getContext(), listener,id);

        //Set layout manager to position the items
        rvPlayer.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //Attach the adapter to the recyclerview to populate items
        rvPlayer.setAdapter(playerAdapter);
    }

}
