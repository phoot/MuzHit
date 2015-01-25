package fr.oxilea.muzhit;

/**
 * Created by philippe on 13/01/2015.
 */
public class Question {

    static final String QCM_TYPE = "1";
    static final String ANSWER_TYPE = "2";

    static final int RIGHT_ANSWER = 1;
    static final int SECOND_ANSWER = 2;
    static final int THIRD_ANSWER = 3;

    private String questionType;
    private String questionYear;
    private String questionText;
    private String questionCorrectAnswer;
    private String questionSecondAnswer;
    private String questionThirdAnswer;
    private String questionIndex;

    // default constructor
    public Question()
    {}

    // full constructor
    public Question(String type, String year, String text, String correctAnswer, String secondAnswer, String thirdAnswer, String index)
    {
        questionType = type;
        questionYear = year;
        questionText = text;
        questionCorrectAnswer = correctAnswer;
        questionSecondAnswer = secondAnswer;
        questionThirdAnswer = thirdAnswer;
        questionIndex = index;
    }

    String GetTypology()
    {
        return questionType;
    }

    void SetTypology(String type)
    {
        questionType = type;
    }

    String GetYear()
    {
        return questionYear;
    }

    void SetYear(String year)
    {
        questionYear = year;
    }

    String GetText()
    {
        return questionText;
    }

    void SetText(String text)
    {
        questionText = text;
    }


    String GetAnswer(int whichAnswer)
    {
        String retStr=null;

        switch (whichAnswer) {
            case RIGHT_ANSWER:
                retStr=questionCorrectAnswer;
                break;
            case SECOND_ANSWER:
                retStr=questionSecondAnswer;
            break;
            case THIRD_ANSWER:
                retStr=questionThirdAnswer;
            break;
        }
        return retStr;
    }

    void SetAnswer(int whichAnswer, String text)
    {
        switch (whichAnswer) {
            case RIGHT_ANSWER:
                questionCorrectAnswer=text;
            break;
            case SECOND_ANSWER:
                questionSecondAnswer=text;
            break;
            case THIRD_ANSWER:
                questionThirdAnswer=text;
            break;
        }
    }

    String GetIndex()
    {
        return questionIndex;
    }

    void SetIndex(String index)
    {
        questionIndex = index;
    }

}
