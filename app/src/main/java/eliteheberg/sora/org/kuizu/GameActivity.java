package eliteheberg.sora.org.kuizu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    public static int ROUND1 = 1;
    public static int ROUND2 = 2;
    public static int ROUND3 = 3;

    double score;
    int currentRound;
    int nbTicks;

    GameActivity gameActivity;
    Button btn1, btn2, btn3;
    TextView tvQuestion, tvRound, tvScore;
    String question;
    String correctAnswer;
    String falseAnswer1;
    String falseAnswer2;

    ProgressWheel pbTime;
    CountDownTimer countDownTimer;
    ArrayList<String> questions;
    ArrayList<String> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game);
        getSupportActionBar().hide();
        gameActivity = this;

        btn1 = (Button) findViewById(R.id.game_btn_resp1);
        btn2 = (Button) findViewById(R.id.game_btn_resp2);
        btn3 = (Button) findViewById(R.id.game_btn_resp3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        tvScore = (TextView) findViewById(R.id.game_tv_score);
        tvRound = (TextView) findViewById(R.id.game_tv_round);

        tvScore.setTypeface(Settings.pixelFlag);

        countDownTimer = new CountDownTimer(10*1000, 100) {

            public void onTick(long millisUntilFinished) {
                double m = millisUntilFinished/(10*1000.0);
                double v = (1-m)*360.0;
                System.out.println("mills = "+millisUntilFinished+" m = "+m+" value = "+v);
                pbTime.setProgress((int)v);
                pbTime.setText((int)(millisUntilFinished/1000)+"");
                nbTicks = (int)millisUntilFinished;
            }

            public void onFinish() {
                new SendDataTask(null).execute();
            }
        };

        pbTime = (ProgressWheel) findViewById(R.id.game_pb_time);
        pbTime.setProgress(0);

        tvQuestion = (TextView) findViewById(R.id.game_tv_question);

        tvQuestion.setTypeface(Settings.textFont);

        questions = new ArrayList<>();
        scores = new ArrayList<>();

        score = 0;
        currentRound = 1;
        nbTicks = 0;

        new GameTask().execute();
    }


    /* no going back mate :) */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        /* disable all, send data and show correct answer */

        countDownTimer.cancel();
        Button btn = (Button) v;

        if (!btn.getText().toString().equals(correctAnswer)) {
            btn.setBackgroundResource(R.drawable.bg_lose);
        }

        if (btn1.getText().toString().equals(correctAnswer))
            btn1.setBackgroundResource(R.drawable.bg_win);
        if (btn2.getText().toString().equals(correctAnswer))
            btn2.setBackgroundResource(R.drawable.bg_win);
        if (btn3.getText().toString().equals(correctAnswer))
            btn3.setBackgroundResource(R.drawable.bg_win);

        btn1.setOnClickListener(null);
        btn2.setOnClickListener(null);
        btn3.setOnClickListener(null);

        new SendDataTask(btn.getText().toString()).execute();
    }


    class GameTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        JSONObject req;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(gameActivity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Reading quizz");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            try {
                req = new JSONObject(Client.in.readLine());
                question = req.getString("question");
                correctAnswer = req.getString("c_answer");
                falseAnswer1 = req.getString("answer1");
                falseAnswer2 = req.getString("answer2");

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            String res[] = new String[3];

            res[0] = correctAnswer;
            res[1] = falseAnswer1;
            res[2] = falseAnswer2;

            Random rand = new Random();
            int Max = 2;
            int Min = 0;
            int randomNum1 = (int) (Math.random() * ((Max - Min) + 1));
            int randomNum2 = 0;
            int randomNum3 = 0;

            do {
                randomNum2 = (int) (Math.random() * ((Max - Min) + 1));
            } while (randomNum1 == randomNum2);

            do {
                randomNum3 = (int) (Math.random() * ((Max - Min) + 1));
            } while ((randomNum3 == randomNum2) || (randomNum3 == randomNum1));


            btn1.setText(res[randomNum1]);
            btn2.setText(res[randomNum2]);
            btn3.setText(res[randomNum3]);

            questions.add(question);

            tvQuestion.setText(question);

            countDownTimer.start();

            tvRound.setText("Round " + currentRound);

            tvScore.setText("[" + score+ "]");
        }
    }

    class SendDataTask extends AsyncTask<Void, Void, Void> {
        JSONObject req;
        String answer;

        public SendDataTask(String answer) {
            currentRound++;
            this.answer = answer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {
                req = new JSONObject();
                if (nbTicks != 0) {
                    req.put("answer", answer);
                    req.put("time", nbTicks);
                } else {
                    req.put("no_answer", 1);
                }

                Client.out.println(req.toString());
                Client.out.flush();

                String res = Client.in.readLine();
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (correctAnswer.equals(answer)) {
                double current =  nbTicks * 50;
                System.out.println("nbTicks = "+nbTicks+ " current score: "+current);

                scores.add(current+"");
                score += current;
            }
            else
                scores.add("0");
            nbTicks = 0;

            if (currentRound <= 3) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn1.setBackgroundResource(R.drawable.button_normal);
                        btn2.setBackgroundResource(R.drawable.button_normal);
                        btn3.setBackgroundResource(R.drawable.button_normal);

                        btn1.setOnClickListener(gameActivity);
                        btn2.setOnClickListener(gameActivity);
                        btn3.setOnClickListener(gameActivity);

                        pbTime.setProgress(0);
                        countDownTimer.start();
                        (new GameTask()).execute();
                    }
                }, 5 * 1000);
            } else {
                /* TODO: run new instance */
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(gameActivity, ResultActivity.class);
                        i.putExtra("score", score);
                        i.putExtra("scores", scores);
                        i.putExtra("questions", questions);
                        startActivity(i);
                    }
                }, 5 * 100);
            }
        }
    }
}
