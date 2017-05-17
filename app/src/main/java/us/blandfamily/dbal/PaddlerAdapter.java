package us.blandfamily.dbal;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.Arrays;

import us.blandfamily.dbal.R;

/**
 * Created by Lachlan on 8/21/2016.
 */
public class PaddlerAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ClubDataStore mData;

    public PaddlerAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
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
            view = mInflater.inflate(R.layout.paddler_entry, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.PaddlerName = (EditText) view.findViewById(R.id.paddlerLongName);
            holder.ShortName = (EditText) view.findViewById(R.id.paddlerShortName);
            holder.Side = (EditText) view.findViewById(R.id.paddlerSide);
            holder.Weight = (EditText) view.findViewById(R.id.paddlerWeight);
            holder.teams[0] = (CheckBox) view.findViewById(R.id.team1);
            holder.teams[1] = (CheckBox) view.findViewById(R.id.team2);
            holder.teams[2] = (CheckBox) view.findViewById(R.id.team3);
            holder.teams[3] = (CheckBox) view.findViewById(R.id.team4);
            holder.teams[4] = (CheckBox) view.findViewById(R.id.team5);
            holder.teams[5] = (CheckBox) view.findViewById(R.id.team6);
            holder.isFemale = (CheckBox) view.findViewById(R.id.isFemale);
            holder.paddlerId = i;

            // hang onto this holder for future recyclage
            view.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) view.getTag();
        }
        // More code after this
        createTextWatchers(paddler, holder);
        holder.PaddlerName.setText(paddler.name);
        holder.ShortName.setText(paddler.shortName);
        holder.Side.setText(paddler.side);
        holder.Weight.setText(String.valueOf(paddler.weight));
        for(int index = 0; index < 6; index++)
        {
            holder.teams[index].setChecked(paddler.teams[index]);
            holder.teams[index].setText(MainActivity.teamLineUps[index].teamName);
        }
        holder.isFemale.setChecked(paddler.female);
        return view;
    }
    private static void createTextWatchers(final PaddlerData paddler, final ViewHolder viewHolder)
    {
        if(viewHolder.PaddlerNameWatcher != null)
        {
            viewHolder.PaddlerName.removeTextChangedListener(viewHolder.PaddlerNameWatcher);
        }
        viewHolder.PaddlerNameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }
            @Override
            public void afterTextChanged(Editable editable) {
                paddler.name = editable.toString();
            }
        };
        viewHolder.PaddlerName.addTextChangedListener(viewHolder.PaddlerNameWatcher);

        if(viewHolder.ShortNameWatcher != null)
        {
            viewHolder.ShortName.removeTextChangedListener(viewHolder.ShortNameWatcher);
        }
        viewHolder.ShortNameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }
            @Override
            public void afterTextChanged(Editable editable) {
                paddler.shortName = editable.toString();
            }
        };
        viewHolder.ShortName.addTextChangedListener(viewHolder.ShortNameWatcher);

        if(viewHolder.SideWatcher != null)
        {
            viewHolder.Side.removeTextChangedListener(viewHolder.SideWatcher);
        }
        viewHolder.SideWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }
            @Override
            public void afterTextChanged(Editable editable) {
                paddler.side = editable.toString();
            }
        };
        viewHolder.Side.addTextChangedListener(viewHolder.SideWatcher);

        if(viewHolder.WeightWatcher != null)
        {
            viewHolder.Weight.removeTextChangedListener(viewHolder.WeightWatcher);
        }
        viewHolder.WeightWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }
            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    paddler.weight = Double.valueOf(editable.toString());
                }
                catch(NumberFormatException e)
                {
                    // don't really do anything here
                }


            }
        };
        viewHolder.Weight.addTextChangedListener(viewHolder.WeightWatcher);
        for(int i = 0; i < 6; i++)
        {
            final int index = i;
            viewHolder.teamsListener[i] = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    paddler.teams[index] = viewHolder.teams[index].isChecked();
                }
            };
            viewHolder.teams[i].setOnClickListener(viewHolder.teamsListener[i]);
        }
        viewHolder.femaleListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paddler.female = viewHolder.isFemale.isChecked();
            }
        };
        viewHolder.isFemale.setOnClickListener(viewHolder.femaleListener);

    }

    // this is used so you only ever have to do
// inflation and finding by ID once ever per View
    private static class ViewHolder {
        public EditText PaddlerName;
        public EditText ShortName;
        public EditText Side;
        public EditText Weight;
        public TextWatcher PaddlerNameWatcher;
        public TextWatcher ShortNameWatcher;
        public TextWatcher SideWatcher;
        public TextWatcher WeightWatcher;
        public CheckBox[] teams = new CheckBox[6];
        public CheckBox isFemale;
        public View.OnClickListener[] teamsListener = new View.OnClickListener[6];
        public View.OnClickListener femaleListener;
        public int paddlerId;
    }
}
