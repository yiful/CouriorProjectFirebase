package com.yiful.couriorprojectfirebase.screen;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yiful.couriorprojectfirebase.R;
import com.yiful.couriorprojectfirebase.model.MyParcel;
import com.yiful.couriorprojectfirebase.util.DatePickerFragment;
import com.yiful.couriorprojectfirebase.util.GetPickerId;
import com.yiful.couriorprojectfirebase.util.TimePickerFragment;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateParcelActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, GetPickerId {
    public static final String TAG = "CreateParcelActivity";
    @BindView(R.id.pickupDate)
    EditText pickupDate;
    @BindView(R.id.deliverTime)
    EditText deliverTime;
    @BindView(R.id.deliveryDate)
    EditText deliveryDate;
    private int id = 0;

    @BindView(R.id.etParcelName)
    EditText etParcelName;
    @BindView(R.id.pickupAddress)
    EditText pickupAddress;
    @BindView(R.id.pickupTime)
    EditText pickupTime;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.deliveryAddress)
    EditText deliveryAddress;

    Bitmap bitmap;
    String bitMapString;
    String status="new";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parcle);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vendor, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
               // NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.pickupTime, R.id.pickupDate, R.id.deliverTime, R.id.deliveryDate, R.id.btnSubmit})
    public void onViewClicked(View view) {
        FragmentManager manager = this.getFragmentManager();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.pickupTime:
                bundle.putInt("id", R.id.pickupTime);
                DialogFragment timePickerFragment = TimePickerFragment.newInstance(bundle);
                timePickerFragment.show(manager, "pickupTimePicker");
                break;
            case R.id.pickupDate:
                bundle.putInt("id", R.id.pickupDate);
                DialogFragment datePickerFragment = DatePickerFragment.newInstance(bundle);
                datePickerFragment.show(manager, "pickupDatePicker");
                break;
            case R.id.deliverTime:
                bundle.putInt("id", R.id.deliverTime);
                DialogFragment timePickerFragment1 = TimePickerFragment.newInstance(bundle);
                timePickerFragment1.show(manager, "deliveryTimePicker");
                break;
            case R.id.deliveryDate:
                bundle.putInt("id", R.id.deliveryDate);
                DialogFragment datePickerFragment1 = DatePickerFragment.newInstance(bundle);
                datePickerFragment1.show(manager, "deliveryDatePicker");
                break;
            case R.id.btnSubmit:
                Log.i(TAG, "btnclicked");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userId = FirebaseAuth.getInstance().getUid();

                //get pickup date and delivery date
                Date date1 = getDateFromString(pickupDate.getText().toString(), pickupTime.getText().toString());
                Date date2 = getDateFromString(deliveryDate.getText().toString(), deliverTime.getText().toString());

                //Modified to add QRCode to firebase
                MyParcel parcel = new MyParcel(userId, etParcelName.getText().toString(), pickupAddress.getText().toString(), date1,
                        deliveryAddress.getText().toString(), date2, spinner.getSelectedItem().toString(), status,
                        pickupAddress.getText().toString());
             //   Map<String, Object> map = parcel.toMap();
                db.collection("parcels").add(parcel)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "parcel stored");
                                    finish();
                                    Toast.makeText(CreateParcelActivity.this, "Your parcel request is created!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Log.w(TAG, "parcel failed to be stored.",task.getException());
                                }
                            }
                        });
        }

    }

    private Date getDateFromString(String date, String time) {
        String str1[] = date.split("/");
        String str2[] = time.split(":");
        //year, month, date, hour, min
        //year - 1900, month -1.
        Date dateFinal = new Date(Integer.valueOf(str1[0])-1900, Integer.valueOf(str1[1])-1, Integer.valueOf(str1[2]),Integer.valueOf(str2[0]),Integer.valueOf(str2[0]));
        return dateFinal;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        EditText etPickupTime = findViewById(id);
        etPickupTime.setText(i + ":" + i1);
        Log.i(TAG, i + ":" + i1);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        EditText etPickupDate = findViewById(id);
        etPickupDate.setText(i + "/" + i1 + "/" + i2);
        Log.i(TAG, i + "/" + i1 + "/" + i2);
    }

    @Override
    public void getPickerId(int i) {
        this.id = i;
    }

}
