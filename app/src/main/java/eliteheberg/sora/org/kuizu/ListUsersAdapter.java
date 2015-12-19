package eliteheberg.sora.org.kuizu;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GaSs on 08/12/2015.
 */
public class ListUsersAdapter extends BaseAdapter {

    ArrayList<User> users;
    private Context context;

    public ListUsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
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
            cview = layoutInflater.inflate(R.layout.layout_listusers_item, null);
        }

        if(users.get(position).getElo() > 2000)
        {
            cview.setBackgroundResource(R.drawable.bg_gold);
        }
        else if(users.get(position).getElo() > 1000)
        {
            cview.setBackgroundResource(R.drawable.bg_silver);
        }
        else if(users.get(position).getElo() > 0)
        {
            cview.setBackgroundResource(R.drawable.bg_bronze);
        }

        TextView userid = (TextView) cview.findViewById(R.id.listusers_tv_userid);
        TextView elo = (TextView) cview.findViewById(R.id.listusers_tv_elo);
        TextView email = (TextView) cview.findViewById(R.id.listusers_tv_email);
        TextView count = (TextView) cview.findViewById(R.id.listusers_tv_number);

        userid.setTypeface(Settings.textFont);

        elo.setTypeface(Settings.pixelFlag);

        count.setTypeface(Settings.pixelFlag);

        count.setText("(" + (position + 1) + ")");

        userid.setText(users.get(position).getUserid());
        elo.setText("<"+users.get(position).getElo()+">");
        email.setText(users.get(position).getEmail());

        return cview;
    }
}
