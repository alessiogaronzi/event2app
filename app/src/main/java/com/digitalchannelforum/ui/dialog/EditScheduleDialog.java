package com.digitalchannelforum.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.digitalchannelforum.drupalcon.R;

public class EditScheduleDialog extends DialogFragment {

    public static final String TAG = EditScheduleDialog.class.getName();
    public static final String EXTRA_SCHEDULE_NAME = "extra_schedule_name";
    public static final String EXTRA_SCHEDULE_CODE = "extra_schedule_code";

    public static EditScheduleDialog newInstance(long code) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_SCHEDULE_CODE, code);

        EditScheduleDialog fragment = new EditScheduleDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static EditScheduleDialog newInstance() {
        return new EditScheduleDialog();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ViewGroup contentView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_shedule_name, null);
        final EditText editTextId = (EditText) contentView.findViewById(R.id.scheduleName);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Schedule name");
        alertDialogBuilder.setView(contentView);
        alertDialogBuilder.setPositiveButton(getActivity().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = editTextId.getText().toString();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent().putExtra(EXTRA_SCHEDULE_NAME, text));

            }
        });

        alertDialogBuilder.setNegativeButton(getActivity().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.favorite));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.favorite));
        return dialog;
    }


}

