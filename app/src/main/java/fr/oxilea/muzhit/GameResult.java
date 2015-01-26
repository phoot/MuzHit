package fr.oxilea.muzhit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class GameResult extends Activity {

    private int scoreGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        Intent myIntent = getIntent();
        scoreGame = myIntent.getIntExtra("scoreGame", 0);

        // set text result
        TextView result = (TextView)findViewById(R.id.gameResultText);
        result.setText(getString(R.string.game_result)+" "+ String.valueOf(scoreGame));
    }

    // switch to game activity
    public void reStartGameActivity(View v)
    {
        Intent intent;

        // create setting activity
        intent = new Intent(GameResult.this, GameMain.class);

        startActivity(intent);
        finish();
    }

}
