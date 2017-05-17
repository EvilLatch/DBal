package us.blandfamily.dbal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PaddlerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paddler_detail);
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        Log.d("Paddler Detail: ", String.valueOf(position));
    }
}
