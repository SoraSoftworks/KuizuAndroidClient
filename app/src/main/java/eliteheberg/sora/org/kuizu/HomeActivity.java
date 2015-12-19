package eliteheberg.sora.org.kuizu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HomeActivity extends AppCompatActivity {
    TextView tvTitle, tvTouchScreen;
    ImageView logo;
    boolean serverOnline;
    View homeNoAction, homeYesAction;
    HomeActivity homeActivity;
    LinearLayout home;
    Button btnLogin, btnRegister;
    EditText etLogLogin, etLogPwd, etRegLogin, etRegEmail, etRegPwd, etRegPhone;
    TabHost tabHost;
    Switch switchRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        homeActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);
        getSupportActionBar().hide();

        home = (LinearLayout)findViewById(R.id.home_ll_panel);
        loadUserConfig();

        Settings.textFont = Typeface.createFromAsset(getAssets(), "fonts/lora/Lora-Regular.ttf");
        Settings.titleFont = Typeface.createFromAsset(getAssets(),"fonts/laffriotnf.regular.ttf");
        Settings.pixelFlag = Typeface.createFromAsset(getAssets(),"fonts/pixelflag/pixelflag.ttf");

        setHomeNoAction();

        logo = (ImageView)findViewById(R.id.home_iv_logo);

        serverOnline = false;

        Settings.music = MediaPlayer.create(this, R.raw.bg_music);
        Settings.music.setLooping(true);

        if(Settings.playMusic)
            if(Settings.music.isPlaying())
                Settings.music.start();

        Settings.music.setVolume(Settings.volume, Settings.volume);

        (new CheckStatusTask()).execute();
    }


    @Override
    protected void onDestroy() {
        if(Settings.music.isPlaying()){
            Settings.music.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (serverOnline)
            new LogoutTask().execute();
    }

    /*
    @Override
    public void onPause() {
        super.onPause();
        if(Settings.music.isPlaying()){
            Settings.music.pause();
        }
    }
*/
    @Override
    public void onResume(){
        super.onResume();
        if(Settings.playMusic)
            if(!Settings.music.isPlaying())
                Settings.music.start();
    }

    void setHomeNoAction()
    {
        homeNoAction = getLayoutInflater().inflate(R.layout.layout_home_no_action, home, false);
        home.addView(homeNoAction);

        homeNoAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serverOnline && Client.dbHelper.getrememberme()) {
                    (new LoginTask(null, null, true)).execute();
                }
                else if(serverOnline) {
                    home.removeView(homeNoAction);
                    setHomeYesAction();
                }
            }
        });

        /* finding views */
        tvTitle = (TextView)findViewById(R.id.home_tv_title);
        tvTouchScreen = (TextView)findViewById(R.id.home_tv_touchscreen);

        tvTitle.setTypeface(Settings.titleFont);
        tvTouchScreen.setTypeface(Settings.textFont);

        final Animation fadeOut = new AlphaAnimation(1, 0.1f);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(2000);
        final Animation fadeIn = new AlphaAnimation(0.1f, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(2000);

        /* cool animations */
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                tvTouchScreen.startAnimation(fadeIn);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                tvTouchScreen.startAnimation(fadeOut);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        tvTouchScreen.startAnimation(fadeOut);
    }

    void setHomeYesAction()
    {
        homeYesAction = getLayoutInflater().inflate(R.layout.layout_home_yes_action, home, false);
        home.addView(homeYesAction);

        /* finding views */
        tabHost = (TabHost)findViewById(R.id.home_th);

        etRegEmail = (EditText)findViewById(R.id.home_et_reg_email);
        etRegLogin= (EditText)findViewById(R.id.home_et_reg_login);
        etRegPwd = (EditText)findViewById(R.id.home_et_reg_pwd);
        etRegPhone = (EditText)findViewById(R.id.home_et_reg_phone);
        etLogLogin = (EditText)findViewById(R.id.home_ed_loglogin);
        etLogPwd = (EditText)findViewById(R.id.home_et_logpwd);

        etRegEmail.setTypeface(Settings.textFont);
        etRegLogin.setTypeface(Settings.textFont);
        etRegPwd.setTypeface(Settings.textFont);
        etRegPhone.setTypeface(Settings.textFont);
        etLogLogin.setTypeface(Settings.textFont);
        etLogPwd.setTypeface(Settings.textFont);
        btnRegister = (Button)findViewById(R.id.home_btn_register);


        /* initializing tabview */
        tabHost.setup();

        TabHost.TabSpec s1 = tabHost.newTabSpec("tag1");
        s1.setIndicator("Login");
        s1.setContent(R.id.home_tab_login);
        tabHost.addTab(s1);

        TabHost.TabSpec s2 = tabHost.newTabSpec("tag2");
        s2.setIndicator("Register");
        s2.setContent(R.id.home_tab_register);
        tabHost.addTab(s2);

        btnLogin = (Button)findViewById(R.id.home_btn_login);

        switchRemember = (Switch)findViewById(R.id.home_switch_remember);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask(etLogLogin.getText().toString(), etLogPwd.getText().toString(), false, switchRemember.isChecked()).execute();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterTask(etRegLogin.getText().toString(), etRegPwd.getText().toString(), etRegEmail.getText().toString(), etRegPhone.getText().toString()).execute();
            }
        });
    }

    void loadUserConfig()
    {
        Settings.conf = "config.json";
        Settings.playMusic = true;
        Settings.volume = 1.0f;

        FileInputStream fileInputStream = null;
        try {
            File file = getBaseContext().getFileStreamPath(Settings.conf);

            if(!file.exists())
                throw new FileNotFoundException();

            fileInputStream = openFileInput(Settings.conf);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String confData = "";

            while(bufferedReader.ready())
            {
                confData += " "+bufferedReader.readLine();
            }
            //Toast.makeText(this, confData, Toast.LENGTH_LONG).show();
            JSONObject obj = new JSONObject(confData);
            Settings.playMusic = obj.getBoolean("playMusic");
            Settings.volume = (float)obj.getDouble("volume");
            fileInputStream.close();
        }catch (FileNotFoundException e) {
            try {
                FileOutputStream fileOutputStream = openFileOutput(Settings.conf, MODE_PRIVATE);
                PrintWriter printWriter = new PrintWriter(fileOutputStream);
                printWriter.write("{\"playMusic\" = true, \"volume\" = 1}");
                printWriter.close();
                Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e1) {
                Toast.makeText(this, "Can not open and create save file, your data wont be saved.", Toast.LENGTH_LONG).show();
            }
        }
        catch (IOException   e){Toast.makeText(this, "ERROR1.", Toast.LENGTH_LONG).show();}
        catch (JSONException e){Toast.makeText(this, "ERROR2.", Toast.LENGTH_LONG).show();}

        Client.dbHelper = new DBHelper(this, DBHelper.DATABASE_NAME, null, 1);

    }

    public class CheckStatusTask extends AsyncTask<Void, Void, Void> {
        protected ProgressDialog progressDialog;
        protected String res;
        protected boolean fine;

        public CheckStatusTask(){}

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(homeActivity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Checking server status");
            progressDialog.setCancelable(false);
            progressDialog.show();
            fine = true;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Client.socket = new Socket(Client.SERVER, Client.PORT);
                Client.in = new BufferedReader(new InputStreamReader(Client.socket.getInputStream()));
                Client.out = new PrintWriter(Client.socket.getOutputStream());

                JSONObject req = new JSONObject();

                req.put("cmd", "status");

                Client.out.println(req);
                Client.out.flush();

                res = Client.in.readLine();
                JSONObject resp = new JSONObject(res);
                fine=true;
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
            if(fine) {
                //Toast.makeText(homeActivity, res, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(homeActivity, "Error occured, server must be busy/offline, please try again later.", Toast.LENGTH_LONG).show();
                tvTouchScreen.setText("Woups, server is offline :(");
                //logo.setImageResource(R.drawable.logo_fail);
            }
            serverOnline = fine;
        }
    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {
        protected ProgressDialog progressDialog;
        protected String res, login, pwd;
        protected boolean fine;
        protected int errorId = 0;
        protected boolean doRemember;

        public LoginTask(String login, String pwd, boolean rememberMe) {
            if(rememberMe){
                this.login = Client.dbHelper.getloginid();
                this.pwd = Client.dbHelper.getpwd();
            }
            else
            {
                this.login = login;
                this.pwd = pwd;
                this.doRemember = false;
            }
        }

        public LoginTask(String login, String pwd, boolean rememberMe, boolean doRemember) {
            if(rememberMe){
                this.login = Client.dbHelper.getloginid();
                this.pwd = Client.dbHelper.getpwd();
            }
            else
            {
                this.login = login;
                this.pwd = pwd;
                this.doRemember = doRemember;
            }
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(homeActivity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Authenticating ..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            if (!login.equals("") && !pwd.equals("")) {
                try {
                    Client.login = login;
                    Client.pwd = pwd;

                    JSONObject req = new JSONObject();

                    req.put("cmd", "login");
                    req.put("userId", login);
                    req.put("pwd", pwd);

                    Client.out.println(req);
                    Client.out.flush();

                    res = Client.in.readLine();
                    JSONObject resp = new JSONObject(res);

                    if (resp.getString("success").equals("1")) {
                        fine = true;
                        Client.rating = resp.getJSONObject("user").getInt("elo");
                    }
                    else
                        fine = false;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                fine = false;
                errorId = 1;
            }

            serverOnline = fine;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if(fine) {
                Toast.makeText(homeActivity, "Welcome  "+login+"!", Toast.LENGTH_LONG).show();
                if(doRemember)
                    Client.dbHelper.rememberme(login, pwd);
                Intent i = new Intent(homeActivity, LobbyActivity.class);
                startActivity(i);
            }
            else
            {
                switch(errorId){
                    case 1:
                        Toast.makeText(homeActivity, "Empty input",Toast.LENGTH_LONG).show();
                        break;
                }
            }

        }
    }

    public class RegisterTask extends AsyncTask<Void, Void, Void> {
        protected ProgressDialog progressDialog;
        protected String res;
        protected boolean fine;
        String login, pwd, email, phone;
        public RegisterTask(String login, String pwd, String email, String phone)
        {
            this.login = login;
            this.pwd = pwd;
            this.email = email;
            this.phone = phone;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(homeActivity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Sending data");
            progressDialog.setCancelable(false);
            progressDialog.show();
            fine = true;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (login.length() < 4 || pwd.length() < 4 || email.length() < 4 || pwd.length() < 4)
                fine = false;
            else
                try {
                    JSONObject req = new JSONObject();

                    req.put("cmd", "signup");
                    req.put("userId", login);
                    req.put("pwd", pwd);
                    req.put("email", email);
                    req.put("phone", phone);

                    Client.out.println(req.toString());
                    Client.out.flush();

                    res = Client.in.readLine();
                    JSONObject resp = new JSONObject(res);
                    if(resp.getInt("success") == 1)
                        fine = true;
                    else
                        fine = false;

                } catch (JSONException e) {
                    fine = false;
                } catch (IOException e) {
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
            if(fine) {
                Toast.makeText(homeActivity, "Welcome aboard "+login+"!", Toast.LENGTH_LONG).show();

                etRegEmail.setText("");
                etRegPhone.setText("");
                etRegLogin.setText("");
                etRegPwd.setText("");

                tabHost.setCurrentTab(0);
            }
            else
            {
                Toast.makeText(homeActivity, "Error occured, one of your inputs already exists.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class LogoutTask extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(homeActivity);
            progressDialog.setTitle("Disconnecting");
            progressDialog.setMessage("Leaving game");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            homeActivity.finish();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Client.out.println("{\"cmd\": \"logout\"}");
            Client.out.flush();
            return null;
        }
    }
}
