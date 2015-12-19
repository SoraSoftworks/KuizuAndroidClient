package eliteheberg.sora.org.kuizu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LobbyActivity extends AppCompatActivity {
    LobbyActivity lobbyActivity;
    LinearLayout home, llContainer;
    View lobbyIdle, lobbyPreGame;
    Button btnJoinQueue, btnViewUsers, btnMatchHistory, btnSettings, btnAccept, btnLogout, btnAbout;
    TextView tvLogin, tvRating;
    ImageView ivLogo;
    ProgressWheel pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lobbyActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lobby);
        getSupportActionBar().hide();
        home = (LinearLayout)findViewById(R.id.lobby_ll_home);
        setIdle();
    }

    void setIdle(){
        lobbyIdle = getLayoutInflater().inflate(R.layout.layout_lobby_idle, home, false);
        home.addView(lobbyIdle);

        btnJoinQueue = (Button)findViewById(R.id.lobby_btn_joinqueue);
        btnViewUsers = (Button)findViewById(R.id.lobby_btn_listusers);
        btnMatchHistory = (Button)findViewById(R.id.lobby_btn_matchhistory);
        btnSettings = (Button)findViewById(R.id.lobby_btn_settings);
        btnLogout = (Button)findViewById(R.id.lobby_btn_quit);
        btnAbout = (Button)findViewById(R.id.lobby_btn_about);
        ivLogo = (ImageView)findViewById(R.id.lobby_iv_logo);
        pbLoading = (ProgressWheel)findViewById(R.id.lobby_pg_loading);


        btnViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lobbyActivity, ListUsersActivity.class);
                startActivity(i);
            }
        });

        btnMatchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lobbyActivity, MatchHistoryActivity.class);
                startActivity(i);
            }
        });

        btnJoinQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QueueTask().execute();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lobbyActivity, SettingsActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Client.dbHelper.getrememberme())
                    Client.dbHelper.logout();
                Intent i = new Intent(lobbyActivity, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lobbyActivity, AboutActivity.class);
                startActivity(i);
            }
        });
    }

    void setPreGame(JSONObject player){
        lobbyPreGame = getLayoutInflater().inflate(R.layout.layout_lobby_pregame, home, false);
        home.addView(lobbyPreGame);
        tvLogin = (TextView)findViewById(R.id.lobby_tv_login);
        tvRating= (TextView)findViewById(R.id.lobby_tv_rating);
        llContainer = (LinearLayout)findViewById(R.id.lobby_ll_container);

        pbLoading.setVisibility(View.GONE);
        ivLogo.setVisibility(View.VISIBLE);

        tvRating.setTypeface(Settings.pixelFlag);

        int elo = 0;
        try {
            elo = player.getInt("elo");
            Client.p2rating = elo;
            Client.loginP2 = player.getString("userId");
            tvLogin.setText(player.getString("userId"));
            tvRating.setText("["+player.getInt("elo")+"]");
        } catch (JSONException e) {
            tvLogin.setText("Unknwown");
            tvRating.setText("?");
        }

        if(elo > 2000)
        {
            llContainer.setBackgroundResource(R.drawable.bg_gold);
        }
        else if(elo > 1000)
        {
            llContainer.setBackgroundResource(R.drawable.bg_silver);
        }
        else if(elo > 0)
        {
            llContainer.setBackgroundResource(R.drawable.bg_bronze);
        }

        btnAccept = (Button)findViewById(R.id.lobby_btn_accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JoinGameTask().execute();
            }
        });
    }

    public class QueueTask extends AsyncTask<Void, Void, Void> {

        protected String res;
        JSONObject player;
        protected JSONObject resp;
        boolean fine = false;
        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            ivLogo.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            pbLoading.startSpinning();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject req = new JSONObject();
                req.put("cmd", "joinQueue");
                Client.out.println(req);
                Client.out.flush();

                res = Client.in.readLine();
                resp = new JSONObject(res);

                if (resp.getString("success").equals("1"))
                    fine = true;
                else
                    throw new QueueFailedException();

                //progressDialog.setMessage("Waiting for an opponent ..");
                //Waiting other player
                res = Client.in.readLine();
                resp = new JSONObject(res);
                if (resp.getString("success").equals("1")) {
                    fine = true;
                    player = resp.getJSONObject("player");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (QueueFailedException e) {
                fine = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (fine) {
                home.removeView(lobbyIdle);
                setPreGame(player);
            }
        }
    }

    public class JoinGameTask extends AsyncTask<Void, Void, Void> {
        protected JSONObject resp;
        protected String res;
        protected boolean fine;
        protected ProgressDialog progressDialog;
        @Override

        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(lobbyActivity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Sending confirmation ..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                resp = new JSONObject();
                resp.put("ready", "1");
                Client.out.println(resp);
                Client.out.flush();

                res = Client.in.readLine();
                resp = new JSONObject(res);
                fine = resp.getInt("success") == 1;

            } catch (IOException e) {
                e.printStackTrace();
                fine = false;
            } catch (JSONException e) {
                e.printStackTrace();
                fine = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            if(fine)
            {
                Intent i = new Intent(lobbyActivity, GameActivity.class);
                startActivity(i);
            }
        }
    }
}
