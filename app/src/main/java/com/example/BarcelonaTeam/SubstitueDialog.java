package com.example.BarcelonaTeam;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SubstitueDialog extends DialogFragment {
    private Player player;
    private int adapterPos;
    private DialogAdapterInterface listener;
    public SubstitueDialog(Player player, int adapterPosition, PlayersAdapter instance) {
        this.player =player;
        adapterPos=adapterPosition;
        listener=instance;
    }

    public interface DialogAdapterInterface {
        void changePlayer(Player player, int adapterPos);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = "Changing Player";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.changePlayer(player,adapterPos);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }

        });
        return builder.create();
    }



}