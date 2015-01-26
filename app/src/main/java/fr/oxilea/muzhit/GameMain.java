package fr.oxilea.muzhit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.Random;


public class GameMain extends Activity {

    private static int ANSWER1=1;
    private static int ANSWER2=2;
    private static int ANSWER3=3;

    private static int GAME_QUESTION_NUMBER=20;

    int currentQuestionIndex;
    int currentCorrectAnswer;
    int currentScore;
    int currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        // initialize score to 000
        currentScore = 0;
        currentQuestion = 1;

        // set first question
        displayNewQuestion(currentScore, currentQuestion);
    }

    // create new question screen
    public void displayNewQuestion(int currentScore, int currentQuestion)
    {

        // score display
        String score = getString(R.string.game_score);
        TextView gameScore = (TextView)findViewById(R.id.gameScore);
        score+= " "+String.valueOf(currentScore);
        gameScore.setText(score);

        // question Number Display
        String QuesNum;
        TextView gameQuestionNumber = (TextView)findViewById(R.id.questionNumber);
        QuesNum = String.valueOf(currentQuestion)+"/"+String.valueOf(GAME_QUESTION_NUMBER);
        gameQuestionNumber.setText(QuesNum);

        // open database and check total number of question
        MusicBdd myQuestionBdd = new MusicBdd(this);
        myQuestionBdd.open();
        int numObj = myQuestionBdd.getObjectCount();

        // choose random question
        Random r = new Random();
        currentQuestionIndex = r.nextInt(numObj)+1;

        Question theQuestion;
        theQuestion = myQuestionBdd.getObjectWithId(currentQuestionIndex);
        myQuestionBdd.close();

        // set text annee
        TextView annee = (TextView)findViewById(R.id.anneeView);
        annee.setText(getString(R.string.annee)+": "+ theQuestion.GetYear());

        // set text question
        TextView question = (TextView)findViewById(R.id.questionText);
        question.setText(theQuestion.GetText());

        // define the display position of the correct answer
        currentCorrectAnswer = r.nextInt(3) + 1;
        if (currentCorrectAnswer == 4)
        {
            currentCorrectAnswer =1;
        }

        String line1Text="1- ";
        String line2Text="2- ";
        String line3Text="3- ";

        // define each line content
        switch (currentCorrectAnswer) {
            case 1:
                line1Text += theQuestion.GetAnswer(Question.RIGHT_ANSWER);
                line2Text += theQuestion.GetAnswer(Question.SECOND_ANSWER);
                line3Text += theQuestion.GetAnswer(Question.THIRD_ANSWER);
                break;

            case 2:
                line1Text += theQuestion.GetAnswer(Question.SECOND_ANSWER);
                line2Text += theQuestion.GetAnswer(Question.RIGHT_ANSWER);
                line3Text += theQuestion.GetAnswer(Question.THIRD_ANSWER);
                break;

            case 3:
                line1Text += theQuestion.GetAnswer(Question.SECOND_ANSWER);
                line2Text += theQuestion.GetAnswer(Question.THIRD_ANSWER);
                line3Text += theQuestion.GetAnswer(Question.RIGHT_ANSWER);
                break;
        }

        TextView answer1 = (TextView)findViewById(R.id.answer1);
        answer1.setText(line1Text);

        TextView answer2 = (TextView)findViewById(R.id.answer2);
        answer2.setText(line2Text);

        TextView answer3 = (TextView)findViewById(R.id.answer3);
        answer3.setText(line3Text);

    }


    // manage button 1 click
    public void OnClickButton1(View v)
    {
        manageAnswer(ANSWER1);
    }

    // manage button 2 click
    public void OnClickButton2(View v)
    {
        manageAnswer(ANSWER2);
    }

    // manage button 3 click
    public void OnClickButton3(View v)
    {
        manageAnswer(ANSWER3);
    }

    // manage answer
    public void manageAnswer(int answerNumber)
    {
        String userFeedback="";

        if (answerNumber == currentCorrectAnswer)
        {
            currentScore+=5;
            userFeedback = getString(R.string.correct_answer);
        }
        else
        {
            userFeedback = getString(R.string.bad_answer);
        }
        // Display feedback to the user, ie answer correct or not
        Toast.makeText(this, userFeedback, Toast.LENGTH_SHORT).show();

        if (currentQuestion < GAME_QUESTION_NUMBER) {
            currentQuestion++;
            displayNewQuestion(currentScore, currentQuestion);
        }
        else
        {
            // end of this game result screen
            startGameResultActivity();

        }
    }

    private void startGameResultActivity()
    {
        Intent intent;

        // create setting activity
        intent = new Intent(GameMain.this, GameResult.class);
        intent.putExtra("scoreGame",currentScore);

        startActivity(intent);
        finish();
    }

}
