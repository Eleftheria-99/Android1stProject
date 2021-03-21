package com.example.dit.hua.ergasia.firstactivity;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBHelper extends SQLiteOpenHelper implements  BaseColumns, Serializable {


    //FIELDS OF THE DB_TABLE
    public static String FIRST_NAME = "_FIRST_NAME";
    public static String LAST_NAME = "_LAST_NAME";
    public static String AGE  ="_AGE";
    //public static final String _ID = "id";

    public static final String DB_NAME = "MY_DATABASE";
    public static final String DB_TABLE = "MY_DATABASE";
    public static final int DB_VERSION =1;

    ArrayList<DataContract> myitems_dbHelper = new ArrayList<DataContract>();
    DataContract datacontract = new DataContract();


     public DBHelper(@Nullable Context context) {                                                   //pulic constructor: to create a helper object to create, open, and/or manage a database.
        super(context, DB_NAME, null, DB_VERSION);                                           //Context: to use for locating paths to the the database

    }
    //context	     Context: to use for locating paths to the the database This value may be null.
    //name	         String: of the database file, or null for an in-memory database This value may be null.
    //factory	     SQLiteDatabase.CursorFactory: to use for creating cursor objects, or null for the default This value may be null.
    //version	     int: number of the database (starting at 1); if the database is older, onUpgrade(SQLiteDatabase, int, int) will be used to upgrade the database; if the database is newer, onDowngrade(SQLiteDatabase, int, int) will be used to downgrade the database

    @Override
    public void onCreate(SQLiteDatabase db) {
        create_database_table(db);                                                                  //create database table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private void create_database_table(SQLiteDatabase db) {        //create database table
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBHelper.FIRST_NAME + " TEXT, " +
                    DBHelper.LAST_NAME + " TEXT, " +
                    DBHelper.AGE + " INTEGER " +
                    ");"
            );
        } catch (Exception e) {
            Log.e(TAG, "DB HELPER CLASS : TABLE NOT CREATED  ", e);
            e.getStackTrace();
        }
    }


    public long createEntry(DataContract contactsContract,  SQLiteDatabase ourDatabase){     //insert the data that the user gave into the databaase table
        ContentValues contentValues = new ContentValues();
        long number = 0;
        //ID is autoincrement
        try {
            contentValues.put(DBHelper.FIRST_NAME, contactsContract.getFname());
            contentValues.put(DBHelper.LAST_NAME, contactsContract.getLname());
            contentValues.put(DBHelper.AGE, contactsContract.getAge());
        }catch(Exception e ){
           Log.e(TAG, "DBHELPER:ERROR-> CREATE ENTRY  ", e);
        }
        //in every column i want to add a new row
        //i have created an object type of DataContract, which contains fname, lname and age , so I need getters to "take" fname, lname and age from that object and put them into database each in the column that belongs
        try{
             number =ourDatabase.insert(DB_TABLE, null , contentValues);
        }catch (Exception e){
            Log.e(TAG, "DBHELPER:ERROR -> INSERT IN CREATE ENTRY ", e);
        }finally {
            Log.i(TAG, "DBHELPER: INSERT IN CREATE ENTRY : DONE ");
        }

        try{
            ourDatabase.close();                                                                    //close connection with the databse in oreder to prevent memory leaking
        }catch(Exception e){
            Log.e(TAG, "DB HELPER CLASS: database connection did not close", e);
            e.getStackTrace();
        }
        return number ;
    }


    public ArrayList<DataContract>  readData (DBHelper dbhelper, String searchfield){    //This method connects with the database, runs a query, stores the results that the query brought into an arraylist and returns the arraylist where the results were stored

        SQLiteDatabase db = dbhelper.getReadableDatabase();                                // When reading data one should always just get a readable database.

        String[] projection = {   // Define a projection that specifies which columns from the database you will actually use after this query.
                BaseColumns._ID,
                DBHelper.FIRST_NAME,
                DBHelper.LAST_NAME,
                DBHelper.AGE
        };

        // Filter results WHERE "fname" =  'serachfield', serachfield = what the user wrote , based on that we will search in the database
        String selection = DBHelper.FIRST_NAME + " = ?";
        String[] selectionArgs = { searchfield };

        //To read from a database, use the query() method, passing it your selection criteria and desired columns
        //The results of the query are returned in a Cursor object.
        Cursor cursor = null;
        try {
            cursor = db.query(
                    DBHelper.DB_NAME,                                                               // The table to query
                    projection,                                                                     // The array of columns to return (pass null to get all)
                    selection,                                                                      // The columns for the WHERE clause
                    selectionArgs,                                                                  // The values for the WHERE clause
                    null,                                                                  // don't group the rows
                    null,                                                                   // don't filter by row groups
                    null);                                                                  // The sort order

        }catch(Exception e){
            e.getStackTrace();
            Log.e(TAG, "DB HELPER CLASS: DB QUERY FAILED ", e);
            e.getCause();
            Thread.dumpStack();
        }finally {
            Log.i(TAG, "DB HELPER CLASS: DB QUERY SUCCEDED");
        }

        //To look at a row in the cursor, use one of the Cursor move methods.
        // Since the cursor starts at position -1, calling moveToNext() places the "read position" on the first entry in the results and returns whether or not the cursor is already past the last entry in the result set.
        // For each row, you can read a column's value by calling one of the Cursor get methods, such as getString() or getInt().
        // For each of the get methods, you must pass the index position of the column you desire, which you can get by calling getColumnIndex() or getColumnIndexOrThrow().
        // When finished iterating through results, call close() on the cursor to release its resources.


        try {
            if(cursor != null && cursor.getCount() > 0){//if (cursor != null) {
                Log.i(TAG, "cursor not null and the numbers of rows in the cursor > 0");

                myitems_dbHelper.clear();

                while (cursor.moveToNext()) {
                    DataContract datacontract = new DataContract();                                 //creating a new object

                    datacontract.setId(cursor.getInt(0));                               //id

                    datacontract.setFname(cursor.getString(1));                         //fname

                    datacontract.setLname(cursor.getString(2));                         //lname

                    datacontract.setAge(cursor.getInt(3));                              //age

                    myitems_dbHelper.add(datacontract);                                             //add new object in the arraylist
                }
            }
            Log.i(TAG, "DB HELPER CLASS: data from cursor passed into arraylist");
        }catch(Exception e){
            e.getStackTrace();
            Log.e(TAG, "DB HELPER CLASS:  passing data from cursor into arraylist failed");
        }finally {
            if (cursor != null && !cursor.isClosed())                                               //if cursor is not null and not closed
                cursor.close();                                                                     //close cursor to avoid memory leaking
            Log.i(TAG, "DB HELPER CLASS: cursor closed ");
        }
        return myitems_dbHelper ;                                                                   //return the arraylist
    }

}
