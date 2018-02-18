package us.blandfamily.dbal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Lachlan on 8/21/2016.
 */
public class PaddlerTeamViewSimpleAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ClubDataStore mData;
    Object currPaddlers[];

    public PaddlerTeamViewSimpleAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mData = new ClubDataStore();

    }
    public void updateData(ClubDataStore newData) {
        // update the adapter's dataset
        mData = newData;
        ArrayList<PaddlerData> tempPaddlers = new ArrayList<PaddlerData>();
        currPaddlers = new PaddlerData[0];
        for(PaddlerData paddler : mData.paddlers.values())
        {
            if(paddler.teams[MainActivity.currTeam] == true)
            {
                tempPaddlers.add(paddler);
            }
        }
        currPaddlers = tempPaddlers.toArray();
        Arrays.sort(currPaddlers);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return currPaddlers.length;
    }

    @Override
    public Object getItem(int i) {
        //Object paddlersObj[] = mData.paddlers.values().toArray();
        //Arrays.sort(paddlersObj);
        if(i < currPaddlers.length)
        {
            return currPaddlers[i];
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        PaddlerData paddler = (PaddlerData) getItem(i);
        if (view == null) {

            // Inflate the custom row layout from your XML.
            view = mInflater.inflate(R.layout.paddler_entry_team_view_simple, null);
            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.ShortName = (TextView) view.findViewById(R.id.paddlerShortName);
            holder.Side = (TextView) view.findViewById(R.id.paddlerSide);
            holder.paddlerId = i;
            holder.itemView = view;
            // hang onto this holder for future recyclage
            view.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) view.getTag();
        }
        // More code after this
        createTextWatchers(paddler, holder);
        holder.ShortName.setText(paddler.shortName);
        holder.Side.setText(paddler.side);
        if(paddler.female)
        {
            holder.itemView.setBackgroundColor(MainActivity.thisActivity.getResources().getColor(R.color.colorFemale));
        }
        else
        {
            holder.itemView.setBackgroundColor(MainActivity.thisActivity.getResources().getColor(R.color.colorMale));
        }

        return view;
    }
    private static void createTextWatchers(final PaddlerData paddler, final ViewHolder viewHolder)
    {
    }

    // this is used so you only ever have to do
// inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView ShortName;
        public TextView Side;
        public int paddlerId;
        public View itemView;
    }
}
