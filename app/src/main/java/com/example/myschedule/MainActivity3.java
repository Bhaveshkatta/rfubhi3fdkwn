package com.example.myschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    Button save_button;
    TextView textView, TextViewCal;
    EditText contacteditext, NameEdittext, Event1, Event2, Event3;
    //EditText NameEdittext;
    int t2Hour, t2Minute;

    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;
    DatabaseReference databas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initDatePicker();
        contacteditext = findViewById(R.id.contactEDittext);
        NameEdittext = findViewById(R.id.NameEdtext);
        TextViewCal = findViewById(R.id.textView5);
        TextViewCal.setText(getTodaysDate());
        textView = findViewById(R.id.textView4);
        Event1 = findViewById(R.id.event1);
        Event2 = findViewById(R.id.event2);
        Event3 = findViewById(R.id.event3);
        save_button = findViewById(R.id.button2);
        databas = FirebaseDatabase.getInstance().getReference().child("Add");

        Intent intent = getIntent();
        String s = intent.getStringExtra("key");
        Intent inte = new Intent(this, MainActivity2.class);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(inte);
                finish();
            }

            
        });
        

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            MainActivity3.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    t2Hour = hourOfDay;
                                    t2Minute = minute;
                                    String time = t2Hour + ":" + t2Minute;
                                    SimpleDateFormat f24Hours = new SimpleDateFormat(
                                            "HH:mm"
                                    );
                                    try {
                                        Date date = f24Hours.parse(time);
                                        SimpleDateFormat f12Hours = new SimpleDateFormat(
                                                "hh:mm aa"
                                        );
                                        textView.setText(f12Hours.format(date));
                                    }catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 12, 0, false
                    );
                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    timePickerDialog.updateTime(t2Hour, t2Minute);
                    timePickerDialog.show();

                }

        });



        NameEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkContactPermission()) {
                    //Permission Granted, pick contact
                    pickContactIntent();
                } else {
                    //Permission not granted, request
                    requestContactPermission();
                }
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(dayOfMonth, month, year);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String Date = makeDateString(dayOfMonth, month, year);
                TextViewCal.setText(Date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, dayOfMonth);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return dayOfMonth + "/" + getMonthFormat(month) + "/" + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "Jan";
        if(month == 2)
            return "Feb";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "Aug";
        if(month == 9)
            return "Sept";
        if(month == 10)
            return "Oct";
        if(month == 11)
            return "Nov";
        if(month == 12)
            return "Dec";

        //Default should never happen
        return "Jan";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    private boolean checkContactPermission() {
        //check if contact permission was granted or not
        boolean result = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED
        );
        return result; //true if permission granted, false if not
    }

    private void requestContactPermission() {
        //permissions to request
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE);
    }

    private void pickContactIntent() {
        //Intent to pick contact
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handle permission request result
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted, can pick a contact now
                pickContactIntent();
            } else {
                //Permission denied
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle intent results
        if (resultCode == RESULT_OK) {
            //calls when a user click a contact from list
            if (requestCode == CONTACT_PICK_CODE) {
                TextView contactTv;
                Cursor cursor1, cursor2;
                //get data from intent
                Uri uri = data.getData();
                cursor1 = getContentResolver().query(uri, null, null, null, null);
                if (cursor1.moveToFirst()) {
//get contact details
                    @SuppressLint("Range") String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    @SuppressLint("Range") String contactThumbnail = ((Cursor) cursor1).getString(cursor1.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                    @SuppressLint("Range") String idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);
                    NameEdittext.setText(contactName);
                    contacteditext.setText("\nName: " + contactName);
                    if (idResultHold == 1) {
                        cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                null,
                                null
                        );
                        //a contact may have multiple phone numbers
                        while (cursor2.moveToNext()) {
// get phone number
                            @SuppressLint("Range") String contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // set details

                            contacteditext.setText("\nPhone: " + contactNumber);
                            //before setting image, check if have or not
//        if(contactThumbnail != null){
//        thumbnailIv.setImageURI(Uri.parse(contactThumbnail));
//        }
//        else{
//        thumbnailIv.setImageResource(R.drawable.ic_person);
//        }
                        }
                        cursor2.close();
                    }
                    cursor1.close();
                }
            }
        } else {
            //calls when user clicks back button I don't pick contact
        }

    }


}


