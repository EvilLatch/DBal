package us.blandfamily.dbal;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Lachlan on 8/21/2016.
 */
public class PaddlerTeamViewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ClubDataStore mData;
    static team_paddlers mTeamView;

    public PaddlerTeamViewAdapter(Context context, LayoutInflater inflater, team_paddlers teamView) {
        mContext = context;
        mInflater = inflater;
        mTeamView = teamView;
        mData = new ClubDataStore();
    }
    public void updateData(ClubDataStore newData) {
        // update the adapter's dataset
        mData = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.paddlers.size();
    }

    @Override
    public Object getItem(int i) {
        Object paddlersObj[] = mData.paddlers.values().toArray();
        Arrays.sort(paddlersObj);
        if(i < paddlersObj.length)
        {
            return paddlersObj[i];
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
            view = mInflater.inflate(R.layout.paddler_entry_team_view, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.ShortName = (TextView) view.findViewById(R.id.paddlerShortName);
            holder.Side = (TextView) view.findViewById(R.id.paddlerSide);
            holder.AddRemove = (Button) view.findViewById(R.id.addRemovePaddler);
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
        return view;
    }
    private static void createTextWatchers(final PaddlerData paddler, final ViewHolder viewHolder)
    {
        viewHolder.buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int teamIndex = MainActivity.currTeam;
                int teamSize = MainActivity.data.getTeamSize(teamIndex);
                int newTeamSize = teamSize + (!paddler.teams[teamIndex] ? 1 : 0);
                if(newTeamSize <= 26)
                {
                    paddler.teams[teamIndex] = !paddler.teams[teamIndex];
                    if(paddler.teams[teamIndex])
                    {
                        viewHolder.AddRemove.setText("REMOVE");
                        if(paddler.female)
                        {
                            viewHolder.itemView.setBackgroundColor(MainActivity.thisActivity.getResources().getColor(R.color.colorFemale));
                        }
                        else
                        {
                            viewHolder.itemView.setBackgroundColor(MainActivity.thisActivity.getResources().getColor(R.color.colorMale));
                        }

                    }
                    else
                    {
                        viewHolder.AddRemove.setText("ADD");
                        if(paddler.female)
                        {
                            viewHolder.itemView.setBackgroundColor(Color.rgb(170, 150, 150));
                        }
                        else
                        {
                            viewHolder.itemView.setBackgroundColor(Color.rgb(150, 150, 170));
                        }

                    }
                    mTeamView.refreshActiveTeamOnly();
                }
                else
                {
                    Toast.makeText(MainActivity.thisActivity, "Too Many Paddlers on Team", Toast.LENGTH_SHORT).show();
                }

                //mTeamView.refresh();
            }
        };
        viewHolder.AddRemove.setOnClickListener(viewHolder.buttonListener);
        int teamIndex = MainActivity.currTeam;
        if(paddler.teams[teamIndex])
        {
            viewHolder.AddRemove.setText("REMOVE");
            if(paddler.female)
            {
                viewHolder.itemView.setBackgroundColor(MainActivity.thisActivity.getResources().getColor(R.color.colorFemale));
            }
            else
            {
                viewHolder.itemView.setBackgroundColor(MainActivity.thisActivity.getResources().getColor(R.color.colorMale));
            }
        }
        else
        {
            viewHolder.AddRemove.setText("ADD");
            if(paddler.female)
            {
                viewHolder.itemView.setBackgroundColor(Color.rgb(170, 150, 150));
            }
            else
            {
                viewHolder.itemView.setBackgroundColor(Color.rgb(150, 150, 170));
            }
        }
    }

    // this is used so you only ever have to do
// inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView ShortName;
        public TextView Side;
        public Button AddRemove;
        public View.OnClickListener buttonListener;
        public int paddlerId;
        public View itemView;
    }
}
