package us.blandfamily.dbal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class club_paddlers extends AppCompatActivity {
    ListView mainListView;
    PaddlerAdapter mArrayAdapter;
    ArrayList mPaddlerList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_paddlers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.new_paddler)
                {
                    int id = MainActivity.data.getUnusedPaddlerId();
                    MainActivity.data.addPaddler(new PaddlerData(id, "zNew Paddler", "zNew", "R/L", 200.0d));
                    Toast.makeText(getBaseContext(), "Added new empty paddler", Toast.LENGTH_SHORT).show();
                    mArrayAdapter.updateData(MainActivity.data);
                    return true;
                }
                if(item.getItemId() == R.id.action_save_data)
                {
                    MainActivity.data.savePaddlers();
                    Toast.makeText(getBaseContext(), "Paddler Data Saved", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        MainActivity parent = (MainActivity) this.getParent();
        mainListView = (ListView) findViewById(R.id.paddler_listview);

        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new PaddlerAdapter(this,getLayoutInflater());
        mArrayAdapter.updateData(parent.data);
        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paddler_menu, menu);
        return true;
    }
}
