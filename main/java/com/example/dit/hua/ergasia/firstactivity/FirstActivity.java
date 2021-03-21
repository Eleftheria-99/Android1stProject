package com.example.dit.hua.ergasia.firstactivity;

import androidx.annotation.RequiresApi;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;



import static android.content.ContentValues.TAG;

public class FirstActivity extends Activity {

    ArrayList<DataContract> myitems = new ArrayList<DataContract>();
    DataContract datacontractobject = new DataContract();

    Button button1 = null;
    Button button2 = null;

    EditText editTextFirstName = null;
    String First_name = null;
    EditText editTextLastName = null;
    String Last_name = null;
    EditText editTextAge = null;
    String Age = null;
    int value = 0;
    boolean didItWork = true;

    EditText editText4 =null;
    String text_4 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity_main);
        setTitle("First Activity");

        click_button1() ;

        click_button2() ;
    }


    protected void click_button1() {        //When the user clicks the button1, pass to variables what the user wrote and save given data to database  !
        button1 = findViewById(R.id.Button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                pass_given_data_variables();                                                        //passing to variables what the user wrote !

                save_data_to_database(First_name, Last_name, value);                                //saving to database the data that the user wrote
            }
        });
    }

    protected void click_button2() {
        //EXPLICIT WAY
        //whenButton button2 = the user clicks the button: button_2 - go to second activity !, check if the search field is empty or not, if it is not empty, search the db and show 2nd activity for results
        button2 = findViewById(R.id.Button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_edit_text_4_into_var_and_beginn_searching_into_db();                           //when the user clicks the button , pass what he wrote in the search field into variable
            }
        });
    }

    protected void pass_given_data_variables() {    //passing to variables what the user wrote !

        //FIRST NAME
        editTextFirstName = findViewById(R.id.Edit_text_first_name);                                // find the EditText, then get its value and put it into variable
        First_name = editTextFirstName.getText().toString();

        if (First_name.isEmpty()) {                                                                 //Check if the EditText is empty, if the user did not write anything, then notify the user
            editTextFirstName.setError("Missing First Name");
        }

        //LAST NAME
        editTextLastName = findViewById(R.id.Edit_text_last_name);                                  // find the EditText, then get its value and put it into variable
        Last_name = editTextLastName.getText().toString();

        if (Last_name.isEmpty()) {                                                                  //Check if the EditText is empty, if the user did not write anything, then notify the user
            editTextLastName.setError("Missing Last name");
        }

        //AGE
        editTextAge = findViewById(R.id.Edit_text_age);                                             // find the EditText, then get its value and put it into variable
        Age = editTextAge.getText().toString();


        if (!"".equals(Age)) {                                                                      //Check if the EditText is empty.  If it is empty, do not try to convert the received value to int. Otherwise, the application will crash.
            value = Integer.parseInt(Age);                                                          //parse the value to Integer
        }

        if (Age.isEmpty()) {                                                                        //  Check if the EditText is empty
            editTextAge.setError("Missing Age!");                                                   //  show error
        }
    }

    protected void save_data_to_database(String First_name1, String Last_name1 , int value1){       //saving to database the data that the user wrote
        try {
            datacontractobject = new DataContract(First_name1, Last_name1, value1);                 //creating an object every time that the user wants to save data
        } catch (Exception e) {
            Log.e(TAG, "OBJECT DATA_CONTRACT_OBJECT NOT CREATED");
            e.getStackTrace();
        }
        final DBHelper entry = new DBHelper(FirstActivity.this);
        final SQLiteDatabase ourDatabase = entry.getWritableDatabase();

        try {
            entry.createEntry(datacontractobject, ourDatabase);                                     //saving the created object to database
            ourDatabase.close();                                                                    //closing connection with the db
        } catch (Exception e) {
            didItWork = false;
            Toast.makeText(FirstActivity.this, "Your data were not saved in database!", Toast.LENGTH_SHORT).show();      //notify the user that the data he gave were not saved
            Log.e(TAG, "FIRST ACIVITY: INSERT FAILED", e);
            e.getStackTrace();
        } finally {
            if (didItWork) {
                Toast.makeText(FirstActivity.this, "Your data were saved in database!", Toast.LENGTH_SHORT).show();       //notify the user that the data he gave were saved
                Log.i(TAG, "FIRST ACIVITY: DATA WERE INSERTED IN DB");
                editTextFirstName.setText(R.string.show_blank);
                editTextLastName.setText(R.string.show_blank);
                editTextAge.setText(R.string.show_blank);
            }
        }
    }

    protected void pass_edit_text_4_into_var_and_beginn_searching_into_db(){                        //when the user clicks the button , pass what he wrote in the search field into a variable
        editText4 = findViewById(R.id.Edit_text_4);
        text_4 = editText4.getText().toString();

        editText4.setText("");                                                                      //edit text will be blank
        if (text_4.isEmpty()) {                                                                     //if search field is empty notify the user, show error
            editText4.setError("Missing search field!");
        } else {                                                                                    //if search field is not empty , search into db , if a result is found,  go to 2nd activity to see results
            search_into_db_and_go_to2nd_activity_for_results();
        }
    }


    protected void search_into_db_and_go_to2nd_activity_for_results() {
        //this method checks if the database returns anything, after executing a query towards the database, based on the first name(search field) that the user gives
        //If nothing is found, the user is notified. If anything is found, then we go to the 2nd activity, where the results are displayed in the table lay out
        DBHelper info = new DBHelper(FirstActivity.this);
        DBHelper dbHelper = new DBHelper(FirstActivity.this);


        if (info.readData(dbHelper, text_4).isEmpty()) {                                            //if the database does not return anything , notify the user
            Toast.makeText(FirstActivity.this, "The db did not return anything!", Toast.LENGTH_SHORT).show();
        } else {                                                                                    //if the database returns soemthing
            myitems.clear();                                                                        //clear the arraylist, in which the results are going to be passed
            try {
                myitems = info.readData(dbHelper, text_4);                                          //pass what the database returned into arraylist
            } catch (Exception e) {
                Toast.makeText(FirstActivity.this, "error on getting data!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "FIRSTACTIVITY failed getting data from info.readData", e);
            }

            try {                                 //close connection to avoid memory leaking
                info.close();
                dbHelper.close();
            } catch (Exception e) {
                e.getStackTrace();
                Log.e(TAG, "info close  ", e);
            }

            Bundle bundle = new Bundle();                                                           //create a Bundle object
            try{
                bundle.putSerializable("arraylist", (Serializable) myitems);

                Intent intent = new Intent();                                                       //create new intent object
                intent.setClassName("com.example.dit.hua.ergasia.firstactivity", "com.example.dit.hua.ergasia.firstactivity.SecondActivity");  //declaring package name and class name

                intent.putExtra("data", bundle);

                startActivity(intent);                                                              //start 2nd activity
            } catch (Exception e) {
                e.getStackTrace();
                Log.e(TAG, "intent  ", e);
            }

        }
    }



}
