package com.shurpo.financialassistant.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.shurpo.financialassistant.R;

public class AboutAppDialogFragment extends DialogFragment{

    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.action_about)).setPositiveButton("yes", onClickListener)
                .setMessage(R.string.about_app_dialog_message);
        return builder.create();
    }


}
