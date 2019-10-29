package com.bignerdranch.geoquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";
    private static final String TAG = "QuizActivity";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),
    };

    private ArrayList<Question> mAnsweredQuestions = new ArrayList<Question>();

    private int mCurrentIndex = 0;
    private int mUserScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");

        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = findViewById(R.id.question_text);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        initQuestion();

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        mPrevButton = findViewById(R.id.prevButton);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                recordQuestionAnswered();
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                recordQuestionAnswered();

                if (mAnsweredQuestions.size() == mQuestionBank.length) {
                    displayResult();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSavedInstanceState() called");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void initQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if (isCurrentQuestionAnswered()) {
            disableTrueFalseButtons();
        }
    }

    private void prevQuestion() {
        if (mCurrentIndex > 0) {
            mCurrentIndex = mCurrentIndex - 1;
        }
        else {
            mCurrentIndex = mQuestionBank.length - 1;
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if (isCurrentQuestionAnswered()) {
            disableTrueFalseButtons();
        }
        else {
            enableTrueFalseButtons();
        }
    }

    private void checkAnswer(boolean userAnswer) {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        int toastText;

        if (currentQuestion.isAnswerTrue() == userAnswer) {
            mUserScore += 1;
            toastText = R.string.correct_toast;
        }
        else {
            toastText = R.string.incorrect_toast;
        }

        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

    public void recordQuestionAnswered() {
        mAnsweredQuestions.add(mQuestionBank[mCurrentIndex]);
    }

    public boolean isCurrentQuestionAnswered() {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        if (mAnsweredQuestions.contains(currentQuestion)) {
            return true;
        }
        return false;
    }

    public void disableTrueFalseButtons() {
        mFalseButton.setClickable(false);
        mTrueButton.setClickable(false);
    }

    public void enableTrueFalseButtons() {
        mFalseButton.setClickable(true);
        mTrueButton.setClickable(true);
    }


    public void displayResult() {
        int result = (mUserScore / mQuestionBank.length) * 100;
        Toast.makeText(this, R.string.result_text + " " + result + "%", Toast.LENGTH_LONG).show();
    }
}

