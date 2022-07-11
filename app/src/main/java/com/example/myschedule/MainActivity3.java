package com.example.myschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {
    Button Cal_button, next_button;
    TextView textView;
    EditText contacteditext;
    EditText NameEdittext;
    int t2Hour, t2Minute;

    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        contacteditext = findViewById(R.id.contactEDittext);
        NameEdittext = findViewById(R.id.NameEdtext);
        Cal_button = findViewById(R.id.CalButton);
        textView = findViewById(R.id.textView4);
        next_button = findViewById(R.id.button2);
        Intent inte = new Intent(this, MainActivity4.class);

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(inte);
                finish();
            }
        });


        Intent intent = getIntent();
        String s = intent.getStringExtra("key");

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


        Cal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar calendar = new calendar();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.CalendarLL,calendar);
                transaction.commit();
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


