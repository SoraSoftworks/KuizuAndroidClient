package eliteheberg.sora.org.kuizu;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sora on 12/12/2015.
 */
public class QuestionDetailsAdapter implements ExpandableListAdapter {
    ArrayList<String> questions;
    ArrayList<String> scores;
    ArrayList<HashMap<String, Object>> childs;
    ArrayList<String> childKeys;

    private Context _context;

    public QuestionDetailsAdapter(Context _context, ArrayList<String> questions, ArrayList<String> scores, JSONArray jsonChild)
    {
        this._context = _context;
        this.questions = questions;
        this.scores = scores;
        childKeys = new ArrayList<>();
        
        childs = new ArrayList<>();

        try {
            for(int i = 0; i < jsonChild.length(); i++)
            {
                HashMap<String, Object> hashMap = new HashMap<>();
                Iterator<String> keys = jsonChild.getJSONObject(i).keys();
                while(keys.hasNext())
                {
                    String s = keys.next();
                    //System.out.println("Key "+s);
                    hashMap.put(s, jsonChild.getJSONObject(i).get(s));
                    childKeys.add(s);
                }
                childs.add(hashMap);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return questions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return childs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).keySet().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_result_groupview, null);
        }

        TextView score = (TextView) convertView.findViewById(R.id.result_tv_qscore);
        TextView question = (TextView) convertView.findViewById(R.id.result_tv_question);

        question.setTypeface(Settings.textFont);
        score.setTypeface(Settings.pixelFlag);
        question.setText(questions.get(groupPosition));
        score.setText("(  Question: "+(groupPosition+1)+"  |  Score:"+scores.get(groupPosition)+"  )");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_result_childview, null);
        }

        TextView key = (TextView)convertView.findViewById(R.id.result_tv_key);
        Object k = childKeys.get(childPosition);
        key.setText(k+": "+childs.get(groupPosition).get(k));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
