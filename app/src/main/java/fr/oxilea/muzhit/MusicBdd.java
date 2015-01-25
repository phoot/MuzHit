package fr.oxilea.muzhit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by philippe on 13/01/2015.
 */
public class MusicBdd {

    static final int RIGHT_ANSWER = 1;
    static final int SECOND_ANSWER = 2;
    static final int THIRD_ANSWER = 3;

    private static final int VERSION_BDD = 2;
    private static final String NOM_BDD = "mymuzhit.db";

    private static final String QUESTION_TABLE="questionTable";
    private static final String ID="Id";
    private static final int NUM_ID=0;
    private static final String COL_QUESTION_TYPOLOGY="Question_Typology";
    private static final int NUM_COL_QUESTION_TYPOLOGY=1;
    private static final String COL_QUESTION_YEAR="Question_Year";
    private static final int NUM_COL_QUESTION_YEAR=2;
    private static final String COL_QUESTION_TEXT="Question_Text";
    private static final int NUM_COL_QUESTION_TEXT=3;
    private static final String COL_QUESTION_RIGHT_ANS="Question_Right_Answer";
    private static final int NUM_COL_QUESTION_RIGHT_ANS=4;
    private static final String COL_QUESTION_SECOND_ANS="Question_Second_Answer";
    private static final int NUM_COL_QUESTION_SECOND_ANS=5;
    private static final String COL_QUESTION_THIRD_ANS="Question_Third_Answer";
    private static final int NUM_COL_QUESTION_THIRD_ANS=6;
    private static final String COL_QUESTION_INDEX="Question_Index";
    private static final int NUM_COL_QUESTION_INDEX=7;


    private SQLiteDatabase bdd;

    private DefBdd myMusicBdd;

    public MusicBdd(Context context){
        // create database a
        myMusicBdd = new DefBdd(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //open database in write mode
        bdd = myMusicBdd.getWritableDatabase();
    }

    public void close(){
        //close database
        bdd.close();
    }


    public int getObjectCount(){
        return (int)(bdd.compileStatement("SELECT COUNT(*) FROM questionTable;").simpleQueryForLong());
    }


    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertObject(Question myQuestion){

        // create ContentValues (behaviour as HashMap)
        ContentValues values = new ContentValues();

        // add each object element)
        values.put(COL_QUESTION_TYPOLOGY, myQuestion.GetTypology());
        values.put(COL_QUESTION_YEAR, myQuestion.GetYear());
        values.put(COL_QUESTION_TEXT, myQuestion.GetText());
        values.put(COL_QUESTION_RIGHT_ANS, myQuestion.GetAnswer(RIGHT_ANSWER));
        values.put(COL_QUESTION_SECOND_ANS, myQuestion.GetAnswer(SECOND_ANSWER));
        values.put(COL_QUESTION_THIRD_ANS,  myQuestion.GetAnswer(THIRD_ANSWER));
        values.put(COL_QUESTION_INDEX,  myQuestion.GetIndex());


        //insert in database
        return bdd.insert(QUESTION_TABLE, null, values);
    }



    public int removeObjectWithID(int id){
        //Remove Object from BDD using the INDEX
        return bdd.delete(QUESTION_TABLE, COL_QUESTION_INDEX + " = " +id, null);
    }

    public Question getObjectWithId(int id){
        // get full object definition
        Cursor c = bdd.query(QUESTION_TABLE, new String[] {ID, COL_QUESTION_TYPOLOGY, COL_QUESTION_YEAR, COL_QUESTION_TEXT, COL_QUESTION_RIGHT_ANS, COL_QUESTION_SECOND_ANS, COL_QUESTION_THIRD_ANS, COL_QUESTION_INDEX}, COL_QUESTION_INDEX + " LIKE \"" + id +"\"", null, null, null, null);
        return cursorToConnectedObject(c);
    }



    //convert cursor to ConnectedObject
    private Question cursorToConnectedObject(Cursor c){
        // if no element found, return null
        if (c.getCount() == 0)
            return null;

        //else set to the first element
        c.moveToFirst();

        //create a ConnectedObject
        Question myObj = new Question();

        //set info from Cursor
        myObj.SetTypology(c.getString(NUM_COL_QUESTION_TYPOLOGY));
        myObj.SetYear(c.getString(NUM_COL_QUESTION_YEAR));
        myObj.SetText(c.getString(NUM_COL_QUESTION_TEXT));
        myObj.SetAnswer(RIGHT_ANSWER,c.getString(NUM_COL_QUESTION_RIGHT_ANS));
        myObj.SetAnswer(SECOND_ANSWER,c.getString(NUM_COL_QUESTION_SECOND_ANS));
        myObj.SetAnswer(THIRD_ANSWER,c.getString(NUM_COL_QUESTION_THIRD_ANS));
        myObj.SetIndex(c.getString(NUM_COL_QUESTION_INDEX));

        //Close the cursor
        c.close();

        // Return ConnectedObject
        return myObj;
    }




}
