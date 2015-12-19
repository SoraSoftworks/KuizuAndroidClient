package eliteheberg.sora.org.kuizu;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

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

public class SettingsActivity extends AppCompatActivity {
    Switch sthmusic;
    SeekBar skvolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        getSupportActionBar().hide();
        skvolume = (SeekBar) findViewById(R.id.settings_volume);
        sthmusic = (Switch) findViewById(R.id.settings_onoff);

        skvolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.volume = progress/100.0f;
                Settings.music.setVolume(Settings.volume, Settings.volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sthmusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.playMusic = isChecked;
                if(!isChecked)
                    Settings.music.stop();
                else
                    Settings.music.start();
            }
        });

        loadSettings();
    }

    void loadSettings () {
        File file = getBaseContext().getFileStreamPath(Settings.conf);
        try {
            if (!file.exists())
                throw new FileNotFoundException();
            FileInputStream fileInputStream = openFileInput(Settings.conf);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String confData = "";
            while (bufferedReader.ready()) {
                confData += " " + bufferedReader.readLine();
            }
            JSONObject obj = new JSONObject(confData);
            Settings.playMusic = obj.getBoolean("playMusic");
            Settings.volume = (float) obj.getDouble("volume");
            fileInputStream.close();
            sthmusic.setChecked(Settings.playMusic);
            skvolume.setProgress((int)(Settings.volume * 100));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void saveSettings () {
        File file = getBaseContext().getFileStreamPath(Settings.conf);
        try {
            if (!file.exists())
                throw new FileNotFoundException();
            FileOutputStream fileOutputStream = openFileOutput(Settings.conf, MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.write("{\"playMusic\" = "+ sthmusic.isChecked() +", \"volume\" = "+ (float)skvolume.getProgress()/100 +"}");
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
    }
}