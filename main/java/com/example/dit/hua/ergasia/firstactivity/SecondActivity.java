package com.example.dit.hua.ergasia.firstactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;


import static android.content.ContentValues.TAG;


public class SecondActivity extends Activity {  //here are being shown the results that have been found in the database, based on the first name that the user wrote

    Context context = SecondActivity.this;
    TableLayout table_lay_out_result = null;

    int id = 0;
    String fname = null;
    String lname = null;
    int age = 0;

    Button buttonGoToFirstActivity = null;

    ArrayList<DataContract> myitems_recover = new ArrayList<DataContract> ();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        setTitle("Second Activity");

        show_results_in_table_lay_out() ;

        click_buton2() ;
    }


    private void show_results_in_table_lay_out() {     //retrieve the results that the 1st activity sent to the 2nd , and show it to the table lay out
        table_lay_out_result = (TableLayout) findViewById(R.id.table_lay_out);

        Intent myintent = getIntent();
        Bundle b = myintent.getBundleExtra("data");

        myitems_recover.clear();
        try {
            myitems_recover = (ArrayList<DataContract>) b.getSerializable("arraylist");        //pass into arraylist the arraylist that the 1st intent sent
        } catch (Exception e) {
            Log.e(TAG, "SECOND ACTIVITY: getting arraylist from 1st intent failed", e);
            e.getStackTrace();
        }

        if (!(myitems_recover.isEmpty())) {                                                         //if the arraylist that the 1st activity sent is not empty
            try {
                for (int i = 0; i < myitems_recover.size(); i++) {                                  //for every object that the arraylist contains, get the values and pass them into variables
                    id = (int) myitems_recover.get(i).getId();
                    fname = (String) myitems_recover.get(i).getFname();
                    lname = (String) myitems_recover.get(i).getLname();
                    age = (int) myitems_recover.get(i).getAge();


                    // Create a new table row.
                    TableRow tableRow = new TableRow(context);

                    // Set new table row layout parameters.
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    tableRow.setLayoutParams(layoutParams);

                    // Add a TextView in the first column.
                    TextView textView1 = new TextView(context);
                    String str = String.valueOf(id);
                    textView1.setText(str);
                    tableRow.addView(textView1, 0);


                    // Add a TextView in the second column
                    TextView textView2 = new TextView(context);
                    textView2.setText(fname);
                    tableRow.addView(textView2, 1);

                    // Add a TextView in the third column.
                    TextView textView3 = new TextView(context);
                    textView3.setText(lname);
                    tableRow.addView(textView3, 2);

                    // Add a TextView in the fourth column.
                    TextView textView4 = new TextView(context);
                    String str_Age = String.valueOf(age);
                    textView4.setText(str_Age);
                    tableRow.addView(textView4, 3);

                    if (tableRow.getParent() != null)
                        ((TableLayout) tableRow.getParent()).removeView(tableRow);
                    table_lay_out_result.addView(tableRow);                                         //add a row in the table lay out
                }
            } catch (Exception e) {
                e.getStackTrace();
                e.getCause();
                Log.e(TAG, "SECOND ACTIVITY: failed showing data to table lay out", e);
            }
        } else {
            Toast.makeText(SecondActivity.this, "Arraylist that the first intent sent was empty", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Arraylist that the first intent sent was empty");
        }
    }


    private void  go_back_to_1st_activity() {    //when a user clicks the button : BACK, then this method will be called, so that the 1st activity will be shown again!
        Intent intent = new Intent();
        intent.setClassName("com.example.dit.hua.ergasia.firstactivity", "com.example.dit.hua.ergasia.firstactivity.FirstActivity");
        startActivity(intent);
    }


    private void click_buton2() {
        //EXPLICIT WAY!!
        //Show 1st activity when the user clicks the button: BACK,  go to first activity !
        buttonGoToFirstActivity = findViewById(R.id.button_go_first_activity);
        buttonGoToFirstActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                go_back_to_1st_activity();
            }
        });
    }

}
