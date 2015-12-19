package eliteheberg.sora.org.kuizu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {
    TextView myId, playerId, myScore, tvPlayerScore, tvResult, tvElo;
    Button btnToLobby;
    LinearLayout llContainer;
    TabHost tabHost;
    double score;
    ArrayList<String> questions, scores;
    protected ExpandableListView expandableListView;

    ResultActivity resultActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resultActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_result);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        score = extras.getDouble("score");

        questions = extras.getStringArrayList("questions");
        scores = extras.getStringArrayList("scores");

        myId = (TextView)findViewById(R.id.result_tv_myid);
        playerId = (TextView)findViewById(R.id.result_tv_playerid);
        myScore = (TextView)findViewById(R.id.result_tv_myscore);
        tvPlayerScore = (TextView)findViewById(R.id.result_tv_playerscore);
        tvResult = (TextView)findViewById(R.id.result_tv_result);
        tvElo = (TextView)findViewById(R.id.result_tv_elo);
        llContainer = (LinearLayout)findViewById(R.id.result_ll_container);

        btnToLobby = (Button)findViewById(R.id.result_btn_tolobby);

        expandableListView = (ExpandableListView)findViewById(R.id.result_elv_info);

        myId.setText(Client.login);
        myScore.setText("" + score);
        playerId.setText(Client.loginP2);

        btnToLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(resultActivity, LobbyActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        /* initializing tabview */
        tabHost = (TabHost)findViewById(R.id.result_tab_stats);
        tabHost.setup();

        TabHost.TabSpec s1 = tabHost.newTabSpec("tag1");
        s1.setIndicator("Summary");
        s1.setContent(R.id.result_tab_summary);
        tabHost.addTab(s1);

        TabHost.TabSpec s2 = tabHost.newTabSpec("tag2");
        s2.setIndicator("Details");
        s2.setContent(R.id.result_tab_details);
        tabHost.addTab(s2);

        new ResultTask().execute();
    }

    /* no going back mate :) */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public class ResultTask extends AsyncTask<Void, Void, Void> {
        protected ProgressDialog progressDialog;
        protected String res;
        protected boolean fine;
        JSONObject req;

        public ResultTask(){}

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(resultActivity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Reading data from server.");
            progressDialog.setCancelable(false);
            progressDialog.show();
            fine = true;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                req = new JSONObject(Client.in.readLine());
            } catch (JSONException e) {
                e.printStackTrace();
                fine = false;
            } catch (IOException e) {
                e.printStackTrace();
                fine = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            try {
                if(req.getBoolean("result")) {
                    llContainer.setBackgroundResource(R.drawable.bg_win);
                    tvResult.setTypeface(Settings.pixelFlag);
                    tvResult.setText("{Victory}");
                    tvResult.setTextSize(45);
                }
                else {
                    tvResult.setText("Defeat");
                    llContainer.setBackgroundResource(R.drawable.bg_lose);
                }

                int newElo = EloRatingSystem.newRating(Client.rating, Client.p2rating, (req.getBoolean("result"))?(EloRatingSystem.SCORE_WIN):EloRatingSystem.SCORE_LOSS);
                int diff = newElo - Client.rating;
                tvElo.setText("Rating: "+newElo+"("+((diff<0)?"":"+")+diff+")");
                Client.rating = newElo;

                expandableListView.setAdapter(new QuestionDetailsAdapter(resultActivity, questions, scores, req.getJSONArray("answers")));

                tvPlayerScore.setText(""+req.getDouble("player_score"));
                Date date = new Date();
                String modifiedDate= new SimpleDateFormat("dd-MM-yyyy").format(date);
                Client.dbHelper.addHisto(Client.loginP2, (int)score, (int)req.getDouble("player_score"), modifiedDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
