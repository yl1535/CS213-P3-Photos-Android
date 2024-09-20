package com.example.photos;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

public class ConfirmDialogFragment extends DialogFragment{

    ConfirmDialogListener listener;
    int type;   //0 -> MainPage Delete
    int data;
    String contains;
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(type==0) builder.setMessage("Are you sure you want to delete Album "+contains+"?");
        else if(type==1) builder.setMessage("Are you sure you want to delete this photo from Album?");
        else if(type==2) builder.setMessage("Are you sure you want to delete this tag from Photo?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                listener.onDialogPositiveClick(ConfirmDialogFragment.this);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDialogNegativeClick(ConfirmDialogFragment.this);
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ConfirmationDialogListener");    //Internal testing unexpected error only
        }
    }

    public void onCancel(DialogInterface dialog){
        super.onCancel(dialog);
        listener.onDialogCancel(ConfirmDialogFragment.this);
    }

    public interface ConfirmDialogListener {
        void onDialogPositiveClick(ConfirmDialogFragment dialog);
        void onDialogNegativeClick(ConfirmDialogFragment dialog);
        void onDialogCancel(ConfirmDialogFragment dialog);
    }
}
