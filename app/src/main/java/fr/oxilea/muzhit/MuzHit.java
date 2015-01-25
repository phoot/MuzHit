package fr.oxilea.muzhit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;


public class MuzHit extends Activity {

    public static String NO_FILE="NO_FILE";

    public static Context ctx;

    private ProgressDialog mProgressDialog;

    public static Context MuzHitContext() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muz_hit);

        // set the context of MuzHit Activity
        ctx = this;

        // check if content must be downloaded
        // open database and check if empty
        MusicBdd myQuestionBdd = new MusicBdd(this);
        myQuestionBdd.open();
        int numObj = myQuestionBdd.getObjectCount();
        myQuestionBdd.close();


        // bdd empty download content from server
        if (numObj == 0)
        {
            if (isNetworkOnline()) {
                // hide start game button until download finished
                Button myButton = (Button) findViewById(R.id.start_game_button);
                myButton.setVisibility(View.INVISIBLE);

                // Create a path where we will place our private file on external
                // storage.
                File file = new File(getExternalFilesDir(null), getString(R.string.dwl_file_name));
                File saveFile;

                // URL to get file
                String questionUrl = getString(R.string.dwl_add);

                // path string to store file
                String filePath = file.getPath();

                mProgressDialog = ProgressDialog.show(this, getString(R.string.downloading),
                        "", true);

                DownloadTask myDownloading = (DownloadTask) new DownloadTask(this, questionUrl, filePath).execute();
            }
            else
            {
                // display feedback to the user and exit button
                String userFeedback="";
                userFeedback = getString(R.string.no_network);

                // hide start game button until download finished
                Button myButton = (Button) findViewById(R.id.start_game_button);
                myButton.setVisibility(View.INVISIBLE);

                // show exit game button until download finished
                myButton = (Button) findViewById(R.id.exit_game_button);
                myButton.setVisibility(View.VISIBLE);

                Toast.makeText(this, userFeedback, Toast.LENGTH_LONG).show();

            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_muz_hit, menu);
        return true;
    }


    public void exitGame(View v) {
        finish();
    }

    public void startNewGame(View v) {
        FileDescriptor fd = null;

        String userFeedback="";
        userFeedback = getString(R.string.no_file);

        // open database and check if empty
        MusicBdd myQuestionBdd = new MusicBdd(this);
        myQuestionBdd.open();
        int numObj = myQuestionBdd.getObjectCount();
        myQuestionBdd.close();

        // bdd empty (new bdd version or first app launch), load bdd from file previously saved from dwl
        if (numObj == 0)  {

            mProgressDialog = ProgressDialog.show(this, getString(R.string.loading_bdd),"", true);

            // do in async mode
            InitBddTask myInit = (InitBddTask) new InitBddTask(this).execute();

            // Hide start game button during bdd load
            Button myButton = (Button) findViewById(R.id.start_game_button);
            myButton.setVisibility(View.INVISIBLE);
        }else
        {
            startGameActivity();
        }
    }


    // switch to game activity
    private void startGameActivity()
    {
        Intent intent;

        // create setting activity
        intent = new Intent(MuzHit.this, GameMain.class);

        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // download task should be async task in android since API level xx
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private String questionUrl;
        private String filePath;

        public DownloadTask(Context context, String qUrl, String fPath) {
            super();

            this.context = context;
            questionUrl = qUrl;
            filePath = fPath;
        }


        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);

            // Show start game button now
            Button myButton = (Button) findViewById(R.id.start_game_button);
            myButton.setVisibility(View.VISIBLE);

            mProgressDialog.cancel();

        }


        @Override
        protected String doInBackground(String... params) {

            String loadResult;

            // launch download
            try {
                loadResult = getFile(questionUrl, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }


        public String getFile(String urlStr, String destFilePath) throws IOException, URISyntaxException {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(destFilePath);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }





    class InitBddTask extends AsyncTask<String, Integer, String> {

        public InitBddTask(Context context) {
            super();
        }


        @Override
        protected String doInBackground(String... params) {

            FileDescriptor fd = null;
            String backgroundResult="OK";

            CsvLoad loadQuestion = new CsvLoad();

            // get descriptor of the file containing new question set
            File file = new File(getExternalFilesDir(null), getString(R.string.dwl_file_name));
            String filePath = file.getAbsolutePath();

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                backgroundResult = NO_FILE;
                e.printStackTrace();
            }
            if (fis != null) {
                try {
                    fd = fis.getFD();
                } catch (IOException e) {
                    backgroundResult = NO_FILE;
                    e.printStackTrace();
                }
            }

            loadQuestion.LoadCsvFile(fd);

            return backgroundResult;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals(NO_FILE)) {

                String userFeedback="";
                userFeedback = getString(R.string.no_file);
                Toast.makeText(getApplicationContext(), userFeedback, Toast.LENGTH_LONG).show();
            }
            else
            {
                startGameActivity();
            }

            mProgressDialog.cancel();
        }
    }


    /*
   ** check network status
    */

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }


}