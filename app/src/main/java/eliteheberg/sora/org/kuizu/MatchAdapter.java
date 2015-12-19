package eliteheberg.sora.org.kuizu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GaSs on 10/12/2015.
 */
public class MatchAdapter extends BaseAdapter {
    public static ArrayList<Match> histo;
    Context context;

    public MatchAdapter(Context context) {
        this.context = context;
        histo = new ArrayList<Match>();
    }

    @Override
    public int getCount() {
        return histo.size();
    }

    @Override
    public Object getItem(int position) {
        return histo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cview = convertView;
        if (cview == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cview = layoutInflater.inflate(R.layout.layout_matchhistory_item, null);
        }

        TextView tvidadvers = (TextView) cview.findViewById(R.id.histo_idadvers);
        TextView tvscore = (TextView) cview.findViewById(R.id.histo_score);
        TextView tvscoreadvers = (TextView) cview.findViewById(R.id.histo_scoreadvers);
        TextView tvdateduel = (TextView) cview.findViewById(R.id.histo_dataduel);

        if(histo.get(position).getScore() > histo.get(position).getScoreadvers())
        {
            cview.setBackgroundResource(R.drawable.bg_win);
        }
        else
        {
            cview.setBackgroundResource(R.drawable.bg_lose);
        }

        tvidadvers.setText(histo.get(position).getIdadvers());
        tvscore.setText(histo.get(position).getScore()+"");
        tvscoreadvers.setText(histo.get(position).getScoreadvers()+"");
        tvdateduel.setText(histo.get(position).getDateduel());

        return cview;
    }
}