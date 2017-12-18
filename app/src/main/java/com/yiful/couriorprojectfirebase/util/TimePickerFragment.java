package com.yiful.couriorprojectfirebase.util;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private GetPickerId pickerId;
    private int id;
    public TimePickerFragment() {
        // Required empty public constructor

    }

    public static TimePickerFragment newInstance(Bundle args){
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.onTimeSetListener = (TimePickerDialog.OnTimeSetListener) context;
        this.pickerId = (GetPickerId) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        id = getArguments().getInt("id");
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.i("TimePickerFragment", i+":"+i1);
        pickerId.getPickerId(id);
        onTimeSetListener.onTimeSet(timePicker, i, i1);

    }
}
