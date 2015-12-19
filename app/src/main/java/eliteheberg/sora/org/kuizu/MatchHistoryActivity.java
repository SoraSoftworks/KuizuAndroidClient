package eliteheberg.sora.org.kuizu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MatchHistoryActivity extends AppCompatActivity {
    MatchHistoryActivity histoActivity;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_matchhistory);
        getSupportActionBar().hide();

        histoActivity = this;

        lvItems = (ListView) findViewById(R.id.matchhistory_lv_hist);
        lvItems.setAdapter(new MatchAdapter(histoActivity));

        MatchAdapter.histo = Client.dbHelper.gethisto();
        lvItems.invalidateViews();
    }
}
