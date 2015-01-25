package fr.oxilea.muzhit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by philippe on 13/01/2015.
 */
public class DefBdd extends SQLiteOpenHelper {

    private static final String QUESTION_TABLE="questionTable";
    private static final String ID="Id";
    private static final String QUESTION_TYPOLOGY="Question_Typology";
    private static final String QUESTION_YEAR="Question_Year";
    private static final String QUESTION_TEXT="Question_Text";
    private static final String QUESTION_RIGHT_ANS="Question_Right_Answer";
    private static final String QUESTION_SECOND_ANS="Question_Second_Answer";
    private static final String QUESTION_THIRD_ANS="Question_Third_Answer";
    private static final String QUESTION_INDEX="Question_Index";

    private static final String CREATE_BDD = "CREATE TABLE " + QUESTION_TABLE + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + QUESTION_TYPOLOGY + " TEXT NOT NULL, "
            + QUESTION_YEAR + " TEXT NOT NULL, "
            + QUESTION_TEXT + " TEXT NOT NULL, "
            + QUESTION_RIGHT_ANS + " TEXT NOT NULL, "
            + QUESTION_SECOND_ANS + " TEXT NOT NULL, "
            + QUESTION_THIRD_ANS + " TEXT NOT NULL, "
            + QUESTION_INDEX + " TEXT NOT NULL);";

    public DefBdd(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table from variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Suppress and create the table again, on new version id restart from 0
        db.execSQL("DROP TABLE " + QUESTION_TABLE + ";");
        onCreate(db);
    }
}