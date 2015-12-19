package eliteheberg.sora.org.kuizu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ListUsersActivity extends AppCompatActivity {
    protected ListUsersActivity listUsersActivity;
    protected ArrayList<User> users;
    ListView lvUsers;
    TextView tvCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listusers);
        getSupportActionBar().hide();
        users = new ArrayList<User>();
        listUsersActivity = this;

        lvUsers = (ListView) findViewById(R.id.listusers_lv_users);
        lvUsers.setAdapter(new ListUsersAdapter(this, users));

        new GetUsersTask().execute();
    }

    public class GetUsersTask extends AsyncTask<Void, Void, Void> {
        protected ProgressDialog progressDialog;
        protected String res;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(listUsersActivity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Reading users list from server");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            User user = null;

            try {
                JSONObject resp;
                JSONObject req = new JSONObject();
                req.put("cmd", "listUsers");

                Client.out.println(req);
                Client.out.flush();

                boolean keep = true;
                String res;

                while (keep) {

                    System.out.println("reading");
                    res = Client.in.readLine();
                    System.out.println("just got "+res);
                    resp = new JSONObject(res);
                    boolean success = resp.getInt("success") == 1;
                    if (!success)
                        keep = false;
                    else {
                        user = new User(resp.getString("userId"), resp.getInt("elo"), resp.getString("email"));
                        users.add(user);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ((ListUsersAdapter)lvUsers.getAdapter()).setUsers(users);
            lvUsers.invalidateViews();
        }
    }

}
