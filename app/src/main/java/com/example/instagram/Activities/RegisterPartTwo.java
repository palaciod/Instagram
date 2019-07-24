package com.example.instagram.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.instagram.Models.FirebaseMethods;
import com.example.instagram.R;

import org.w3c.dom.Text;

import java.util.Calendar;

public class RegisterPartTwo extends AppCompatActivity {
    private Button gender_is_woman, gender_is_man;
    private int myGenderIsMale;
    private int myGenderIsFemale;
    private TextView birthday_picker;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView phoneNumber;
    private int age;
    private TextView nameTextView;
    private String bday;
    private FirebaseMethods fbMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_part_two);
        age = 0;
        myGenderIsMale = 0;
        myGenderIsFemale = 0;
        datePicker();
        fbMethods = new FirebaseMethods(RegisterPartTwo.this);

    }
    public void toNextPage(View view){
        nameTextView = findViewById(R.id.intro_layout_first_name);
        phoneNumber = findViewById(R.id.phone_number_info);
        fbMethods.write_to_database(nameTextView.getText().toString(),bday,age,phoneNumber.getText().toString(),gender_selected(),getUserName());
        Intent intent = new Intent(RegisterPartTwo.this,UploadProfilePicture.class);
        startActivity(intent);
        finish();
    }
    public String gender_selected(){
        if(myGenderIsMale==1){
            return "male";
        }
        if(myGenderIsFemale==1){
            return "female";
        }
        return null;
    }
    public void my_gender_is_male(View view){
        gender_is_man = (Button) findViewById(R.id.intro_layout_man_gender);
        gender_is_woman = (Button) findViewById(R.id.intro_layout_woman_gender);
        int alpha_female = gender_is_woman.getBackground().getAlpha();
        if(alpha_female<255){
            gender_is_woman.getBackground().setAlpha(255);
            myGenderIsFemale = 0;
        }
        gender_is_man.getBackground().setAlpha(200);
        myGenderIsMale = 1;
        System.out.println("Integer value for male is: "+myGenderIsMale+". Female integer value is: "+ myGenderIsFemale);
    }

    public void my_gender_is_female(View view){
        gender_is_woman = (Button) findViewById(R.id.intro_layout_woman_gender);
        gender_is_man = (Button) findViewById(R.id.intro_layout_man_gender);
        int alpha_male = gender_is_man.getBackground().getAlpha();
        if(alpha_male<255){
            gender_is_man.getBackground().setAlpha(255);
            myGenderIsMale = 0;
        }
        gender_is_woman.getBackground().setAlpha(200);
        myGenderIsFemale = 1;
        System.out.println("Integer value for male is: "+myGenderIsMale+". Female integer value is: "+ myGenderIsFemale);
    }
    /*
    --------The datePicker method will handle the birthday date setter--------------
     */
    public void datePicker(){
        birthday_picker = (TextView) findViewById(R.id.bday_intro);
        birthday_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                System.out.println("This is the current month: " +month);

                /*
                -------- The holo light below might change to different theme and the transparency once clicked might change--------------
                 */

                DatePickerDialog dateDialog = new DatePickerDialog(RegisterPartTwo.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();

            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                System.out.println("onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = month + "/" + dayOfMonth + "/" + year;
                bday = date;
                age = get_age(year,month,dayOfMonth);
                System.out.println("My age is: "+age);
                birthday_picker.setText(date);
            }
        };
    }

    public int get_age(int birth_year,int birth_month,int birth_day){
        Calendar cal = Calendar.getInstance();
        int current_year = cal.get(Calendar.YEAR);
        int current_month = cal.get(Calendar.MONTH) + 1;
        int current_day = cal.get(Calendar.DAY_OF_MONTH);
        System.out.println("This is my year: "+current_year+" This is my month: "+current_month+" This is my day: "+current_day);
        System.out.println("This is my year: "+birth_year+" This is my month: "+birth_month+" This is my day: "+birth_day);
        int age = current_year - birth_year;
        int month_value = current_month - birth_month;
        if(month_value<0){
            age--;
        }else if(month_value==0){
            int day_value = current_day - birth_day;
            if(day_value>0){
                age--;
            }
        }

        return age;
    }
    // Will return the beginning part of your email as a default username.
    public String getUserName(){
        String username = "";
        String email = fbMethods.getEmail();
        for (int i = 0; i < email.length();i++){
            if(email.charAt(i) == '@'){
                break;
            }
            username = username + email.charAt(i);
        }
        return username;
    }
}
