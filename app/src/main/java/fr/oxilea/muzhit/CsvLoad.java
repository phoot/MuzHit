package fr.oxilea.muzhit;

import com.opencsv.CSVReader;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by philippe on 14/01/2015.
 */
public class CsvLoad {


    public Boolean LoadCsvFile (FileDescriptor csvFd) {
        Boolean readOk = true;

        String[] row = null;

        CSVReader csvReader = null;

        // csv with ; as separator
        csvReader = new CSVReader(new FileReader(csvFd),';');

        List content = null;
        if (csvReader != null) {
            try {
                content = csvReader.readAll();
            } catch (IOException e) {
                e.printStackTrace();
                readOk = false;
            }
        }

        // read the content and save it to database
        //  total number of questions
        int totalNqQuestions = 0;
        Question theQuestion;

        // open database
        MusicBdd myQuestionBdd = new MusicBdd(MuzHit.MuzHitContext());
        myQuestionBdd.open();

        if(content != null) {
            for (Object object : content) {
                row = (String[]) object;

                // one more question
                totalNqQuestions++;

                // insert all question in database
                theQuestion = new Question (row[1], row[2], row[3], row[4], row[5], row[6], String.valueOf(totalNqQuestions));
                myQuestionBdd.insertObject(theQuestion);

            }
        }
//...
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                readOk = false;
            }
        }
        return readOk;
    }


}
