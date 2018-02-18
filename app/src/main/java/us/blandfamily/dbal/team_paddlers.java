package us.blandfamily.dbal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class team_paddlers extends AppCompatActivity {
    ListView mainListView;
    ListView teamListView;
    PaddlerTeamViewAdapter mArrayAdapter;
    ArrayList mPaddlerList = new ArrayList();
    PaddlerTeamViewSimpleAdapter mArraySimpleAdapter;
    ArrayList mPaddlerSimpleList = new ArrayList();
    TextView mTeamSizeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_paddlers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final team_paddlers thisActivity = this;
        TextView teamName = (TextView) findViewById(R.id.TeamDisplay);
        mTeamSizeView = (TextView) findViewById(R.id.teamSize);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_save_data)
                {
                    MainActivity.saveAllData();
                    Toast.makeText(getBaseContext(), "Paddler Data Saved", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
        teamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamSelectorDialog.launchTeamSelectorDialog(thisActivity);
                thisActivity.refresh();
            }
        });

        refresh();

    }

    public void refresh() {
        TextView teamName = (TextView) findViewById(R.id.TeamDisplay);
        mTeamSizeView = (TextView) findViewById(R.id.teamSize);
        teamName.setText(MainActivity.teamLineUps[MainActivity.currTeam].teamName);
        mTeamSizeView.setText(String.format("Team Size: %d", MainActivity.data.getTeamSize(MainActivity.currTeam)));
        MainActivity parent = (MainActivity) this.getParent();
        mainListView = (ListView) findViewById(R.id.paddler_listview);
        teamListView = (ListView) findViewById(R.id.team_listview);

        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new PaddlerTeamViewAdapter(this,getLayoutInflater(), this);
        mArrayAdapter.updateData(parent.data);
        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);
        // Create an ArrayAdapter for the ListView
        mArraySimpleAdapter = new PaddlerTeamViewSimpleAdapter(this,getLayoutInflater());
        mArraySimpleAdapter.updateData(parent.data);
        // Set the ListView to use the ArrayAdapter
        teamListView.setAdapter(mArraySimpleAdapter);

    }

    public void refreshActiveTeamOnly() {
        TextView teamName = (TextView) findViewById(R.id.TeamDisplay);
        teamName.setText(MainActivity.teamLineUps[MainActivity.currTeam].teamName);
        mTeamSizeView = (TextView) findViewById(R.id.teamSize);
        mTeamSizeView.setText(String.format("Team Size: %d", MainActivity.data.getTeamSize(MainActivity.currTeam)));
        MainActivity parent = (MainActivity) this.getParent();
        //mainListView = (ListView) findViewById(R.id.paddler_listview);
        teamListView = (ListView) findViewById(R.id.team_listview);

        // Create an ArrayAdapter for the ListView
        //mArrayAdapter = new PaddlerTeamViewAdapter(this,getLayoutInflater(), this);
        //mArrayAdapter.updateData(parent.data);
        // Set the ListView to use the ArrayAdapter
        //mainListView.setAdapter(mArrayAdapter);
        // Create an ArrayAdapter for the ListView
        mArraySimpleAdapter = new PaddlerTeamViewSimpleAdapter(this,getLayoutInflater());
        mArraySimpleAdapter.updateData(parent.data);
        // Set the ListView to use the ArrayAdapter
        teamListView.setAdapter(mArraySimpleAdapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paddler_menu, menu);
        return true;
    }
}
