package us.blandfamily.dbal;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,View.OnDragListener, Toolbar.OnClickListener {

    public static MainActivity thisActivity;
    private TextView[] leftPaddlers = new TextView[10];
    private TextView[] rightPaddlers = new TextView[10];
    public TextView teamDisplay;
    public TextView lineUpDisplay;
    public TextView teamCount;

    static private int teamSize = 26;
    static private int crewSize = 22;

    Map<Integer, PaddlerData> paddlerLookup = new HashMap<Integer, PaddlerData>(); // map from paddlerListID to paddler data
    Map<Integer, Integer> positionLookup = new HashMap<Integer, Integer>(); // map from seat id to seat index
    Map<Integer, Integer> teamLookup = new HashMap<Integer, Integer>(); // map paddlerId to index in display of team
    private TextView drummer;
    private TextView tiller;
    final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 2;
    final int MY_PERMISSIONS_REQUEST_INTERNET = 3;
    final int MY_PERMISSIONS_REQUEST_READ_PHONE = 4;
    final int MY_PERMISSIONS_REQUEST_CORSE_LOCATION = 5;
    final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 6;

    private TextView[] paddlers = new TextView[teamSize];  // view of the whole team
    private TextView[] paddlerPositionsDisplay = new TextView[crewSize]; //text display for people in the boat
    private TextView[] paddlerWeightDisplay = new TextView[crewSize]; // weights displayed for people in the boat

    private TextView frontLeftField;
    private TextView frontRightField;
    private TextView backLeftField;
    private TextView backRightField;
    private TextView frontField;
    private TextView backField;
    private TextView leftField;
    private TextView rightField;

    static public int currTeam = 0;
    static public int currLineUp = 0;
    static public int nextTeam = 0;
    static public int nextLineUp = 0;

    static boolean init = true;
    //static int[] paddlerPositions = new int[crewSize];
    public static TeamLineUps[] teamLineUps = new TeamLineUps[6];
    public static ClubDataStore data = new ClubDataStore();

    public void createLoadLineUps()
    {
        for(int i = 0; i < 6; i++)
        {
            // check to see if the line up has been created / loaded yet
            if(teamLineUps[i] == null)
            {
                teamLineUps[i] = new TeamLineUps(i);
                if(!teamLineUps[i].loadLineUps())
                {
                    teamLineUps[i].saveLineUps();
                }
                else
                {
                    teamLineUps[i].verifyAndSanitizeLineUp(data);
                }
            }
        }

    }

    public static void saveAllLineUps()
    {
        for(int i = 0; i < 6; i++)
        {
            teamLineUps[i].saveLineUps();
        }
    }

    public void createPaddlerData()
    {
        for(int i = 1; i < 100; i++)
        {
            PaddlerData p1 = new PaddlerData(i, String.format("Paddler %d", i) , String.format("P_%d", i), "R/L", Math.floor(110 + (int)(Math.random() * 100)));
            if(((int)(Math.random() * 100)) > 50)
            {
                p1.female = true;
            }
            data.addPaddler(p1);
        }
        //data.savePaddlers();
        for(int i = 0; i < 6; i++)
        {
            teamLineUps[i] = new TeamLineUps(i);
            teamLineUps[i].teamName = String.format("Team_%d", i+1);
            //teamLineUps[i].loadLineUpsFromCSV(csvRead);
        }
        saveAllData();
    }

    public void createMappings()
    {
        paddlers[0] = (TextView)findViewById(R.id.paddler1);
        paddlers[1] = (TextView)findViewById(R.id.paddler2);
        paddlers[2] = (TextView)findViewById(R.id.paddler3);
        paddlers[3] = (TextView)findViewById(R.id.paddler4);
        paddlers[4] = (TextView)findViewById(R.id.paddler5);
        paddlers[5] = (TextView)findViewById(R.id.paddler6);
        paddlers[6] = (TextView)findViewById(R.id.paddler7);
        paddlers[7] = (TextView)findViewById(R.id.paddler8);
        paddlers[8] = (TextView)findViewById(R.id.paddler9);
        paddlers[9] = (TextView)findViewById(R.id.paddler10);
        paddlers[10] = (TextView)findViewById(R.id.paddler11);
        paddlers[11] = (TextView)findViewById(R.id.paddler12);
        paddlers[12] = (TextView)findViewById(R.id.paddler13);
        paddlers[13] = (TextView)findViewById(R.id.paddler14);
        paddlers[14] = (TextView)findViewById(R.id.paddler15);
        paddlers[15] = (TextView)findViewById(R.id.paddler16);
        paddlers[16] = (TextView)findViewById(R.id.paddler17);
        paddlers[17] = (TextView)findViewById(R.id.paddler18);
        paddlers[18] = (TextView)findViewById(R.id.paddler19);
        paddlers[19] = (TextView)findViewById(R.id.paddler20);
        paddlers[20] = (TextView)findViewById(R.id.paddler21);
        paddlers[21] = (TextView)findViewById(R.id.paddler22);
        paddlers[22] = (TextView)findViewById(R.id.paddler23);
        paddlers[23] = (TextView)findViewById(R.id.paddler24);
        paddlers[24] = (TextView)findViewById(R.id.paddler25);
        paddlers[25] = (TextView)findViewById(R.id.paddler26);

        teamCount = (TextView)findViewById(R.id.paddlerCount);
        for(int i = 0; i < teamSize; i++)
        {
            if(paddlers[i] != null)
            {
                paddlers[i].setOnTouchListener(this);
                paddlers[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
        leftPaddlers[0] = (TextView)findViewById(R.id.Left1);
        leftPaddlers[1] = (TextView)findViewById(R.id.Left2);
        leftPaddlers[2] = (TextView)findViewById(R.id.Left3);
        leftPaddlers[3] = (TextView)findViewById(R.id.Left4);
        leftPaddlers[4] = (TextView)findViewById(R.id.Left5);
        leftPaddlers[5] = (TextView)findViewById(R.id.Left6);
        leftPaddlers[6] = (TextView)findViewById(R.id.Left7);
        leftPaddlers[7] = (TextView)findViewById(R.id.Left8);
        leftPaddlers[8] = (TextView)findViewById(R.id.Left9);
        leftPaddlers[9] = (TextView)findViewById(R.id.Left10);

        rightPaddlers[0] = (TextView)findViewById(R.id.Right1);
        rightPaddlers[1] = (TextView)findViewById(R.id.Right2);
        rightPaddlers[2] = (TextView)findViewById(R.id.Right3);
        rightPaddlers[3] = (TextView)findViewById(R.id.Right4);
        rightPaddlers[4] = (TextView)findViewById(R.id.Right5);
        rightPaddlers[5] = (TextView)findViewById(R.id.Right6);
        rightPaddlers[6] = (TextView)findViewById(R.id.Right7);
        rightPaddlers[7] = (TextView)findViewById(R.id.Right8);
        rightPaddlers[8] = (TextView)findViewById(R.id.Right9);
        rightPaddlers[9] = (TextView)findViewById(R.id.Right10);

        tiller = (TextView)findViewById(R.id.Tiller);
        drummer = (TextView)findViewById(R.id.Drummer);

        TextView emptySlot = (TextView)findViewById(R.id.emptypaddler);
        emptySlot.setOnTouchListener(this);

        int index = 0;
        for(int i = 0; i < 10; i++)
        {
            paddlerPositionsDisplay[index] = leftPaddlers[i];
            positionLookup.put(leftPaddlers[i].getId(), index++);
            paddlerPositionsDisplay[index] = rightPaddlers[i];
            positionLookup.put(rightPaddlers[i].getId(), index++);
            leftPaddlers[i].setOnDragListener(this);
            rightPaddlers[i].setOnDragListener(this);
            leftPaddlers[i].setOnTouchListener(this);
            rightPaddlers[i].setOnTouchListener(this);

        }
        paddlerPositionsDisplay[index] = drummer;
        positionLookup.put(drummer.getId(), index++);
        paddlerPositionsDisplay[index] = tiller;
        positionLookup.put(tiller.getId(), index++);
        drummer.setOnDragListener(this);
        tiller.setOnDragListener(this);
        drummer.setOnTouchListener(this);
        tiller.setOnTouchListener(this);

        index = 0;
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight1);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight1);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight2);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight2);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight3);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight3);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight4);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight4);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight5);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight5);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight6);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight6);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight7);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight7);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight8);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight8);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight9);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight9);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.LeftWeight10);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.RightWeight10);

        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.DrummerWeight);
        paddlerWeightDisplay[index++] = (TextView)findViewById(R.id.TillerWeight);


        frontLeftField = (TextView)findViewById(R.id.FrontLeftWeight);
        frontRightField = (TextView)findViewById(R.id.FrontRightWeight);
        backLeftField = (TextView)findViewById(R.id.BackLeftWeight);
        backRightField = (TextView)findViewById(R.id.BackRightWeight);
        frontField = (TextView)findViewById(R.id.FrontWeight);
        backField = (TextView)findViewById(R.id.BackWeight);
        leftField = (TextView)findViewById(R.id.LeftWeight);
        rightField = (TextView)findViewById(R.id.RightWeight);

        thisActivity = this;
        teamDisplay = (TextView)findViewById(R.id.TeamDisplay);
        lineUpDisplay = (TextView)findViewById(R.id.LineUpDisplay);
        teamDisplay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TeamSelectorDialog.teamNameEdit(thisActivity);
                return true;
            }
        });
        teamDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamSelectorDialog.launchTeamSelectorDialog(thisActivity);
            }
        });
        lineUpDisplay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TeamSelectorDialog.lineUpNameEdit(thisActivity);
                return true;
            }
        });
        lineUpDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamSelectorDialog.launchLineUpSelectorDialog(thisActivity);
            }
        });
    }

    public void setTeamLineUpActive()
    {
        // given the current team and lineUp as being active and update the UI display to match

        // The first section here maps the paddler section down the bottom to only show paddlers in the current team
        teamLineUps[currTeam].verifyAndSanitizeLineUp(data);
        //String[] teams = getResources().getStringArray(R.array.Teams);
        String lineUpName = teamLineUps[currTeam].getLineUpName(currLineUp);
        teamDisplay.setText(teamLineUps[currTeam].teamName);
        lineUpDisplay.setText(lineUpName);
        paddlerLookup.clear();
        teamLookup.clear();
        for(int i = 0; i < teamSize; i++)
        {
            paddlers[i].setText("Vacant Slot");
        }
        int numPaddlers = 0;
        int index = 0;
        // May need to set this up to be on a per team basis
        Object rawPaddlers[] = data.paddlers.values().toArray();
        Arrays.sort(rawPaddlers);
        for(Object obj : rawPaddlers)
        {
            PaddlerData paddler = (PaddlerData) obj;
            if(index < paddlers.length && paddler.teams[currTeam] == true)
            {
                numPaddlers++;
                paddlers[index].setText(paddler.name);
                teamLookup.put(paddler.id, index);
                int id = paddlers[index++].getId();
                paddlerLookup.put(id, paddler);
            }
        }
        teamCount.setText("Team Size: " + String.valueOf(numPaddlers));

        // This second section should populate the current lineUp
        // for every position in the team, find the paddler and update name and weight
        for(int i = 0; i < crewSize; i++)
        {
            int paddlerId = teamLineUps[currTeam].getPaddlerInLineUp(i, currLineUp);
            PaddlerData paddler = data.getPaddler(paddlerId);
            if(paddler != null)
            {
                paddlerPositionsDisplay[i].setText(paddler.shortName + " | " + paddler.side);
                paddlerWeightDisplay[i].setText(Double.toString(paddler.weight));
            }
            else
            {
                paddlerPositionsDisplay[i].setText("Empty");
                paddlerWeightDisplay[i].setText("-");
            }
        }
        updateDisplay();
    }

    public void setupText()
    {
        for(int i = 0; i < teamSize; i++)
        {
            PaddlerData data = paddlerLookup.get(paddlers[i].getId());
            if(data != null)
            {
                paddlers[i].setText(data.name);
                //paddlers[i].setText(data.name + " | " + data.side);
            }

        }

//        for(int i = 0; i < crewSize; i++)
//        {
//
//            PaddlerData paddler = paddlerLookup.get(paddlerPositions[i]);
//            if(paddler != null)
//            {
//                paddlerPositionsDisplay[i].setText(paddler.name + " | " + paddler.side );
//                //paddlerPositions[index] = dropped.getId();
//                paddlerWeightDisplay[i].setText(Double.toString(paddler.weight));
//            }
//            else
//            {
//                paddlerPositionsDisplay[i].setText("Empty");
//                paddlerPositions[i] = 0;
//                paddlerWeightDisplay[i].setText("-");
//
//            }
//            //paddlerPositionsDisplay[i].setText();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckSetPermissions();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final MainActivity thisActivity = this;
        final Intent fileSelectIntent = new Intent(this, FileChooser.class);
        final Intent clubPaddlersIntent = new Intent(this, club_paddlers.class);
        final Intent teamPaddlersIntent = new Intent(this, team_paddlers.class);
        final Intent strokeRateIntent = new Intent(this, strokeRate.class);

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_paddler_details)
                {
                    // create an Intent to take you over to a new DetailActivity

                    // start the next Activity using your prepared Intent
                    startActivity(clubPaddlersIntent);
                    return true;

                }
                if(item.getItemId() == R.id.action_save_data)
                {
                    // start the next Activity using your prepared Intent
                    TeamSelectorDialog.launchSaveDialog(thisActivity);
                    return true;
                }
                if(item.getItemId() == R.id.team_spinner)
                {
                    // start the next Activity using your prepared Intent
                    startActivity(teamPaddlersIntent);
                    return true;
                    //// start the next Activity using your prepared Intent
                    //TeamSelectorDialog.launchTeamSelectorDialog(thisActivity);
                    //return true;
                }
                if(item.getItemId() == R.id.lineup_spinner)
                {
                    // start the next Activity using your prepared Intent
                    TeamSelectorDialog.launchLineUpCopyDialog(thisActivity);
                    //TeamSelectorDialog.launchLineUpSelectorDialog(thisActivity);
                    return true;
                }
                if(item.getItemId() == R.id.action_loadSpecific) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.thisActivity);
                    builder.setTitle("Save Before Load?");
                    builder.setMessage("Do you want to save before overwriting with a load?")
                            // Add the buttons
                            .setPositiveButton("Yes, Save before loading", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button
                                    SaveFileDialog(true);
                                }
                            })
                            .setNegativeButton("Discard and Load Anyway", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    LoadFileDialog();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
                if(item.getItemId() == R.id.action_saveSpecific) {
                    SaveFileDialog(false);
                    return true;
                }
                return false;
            }
        });

        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }
        if(savedInstanceState == null)
        {
            File importDir = new File(Environment.getExternalStorageDirectory(), "");
            File file = new File(importDir, "MasterPaddlers.csv");
            if(data.uberLoadPaddlers(file) == false)
            {
                createPaddlerData();
                //createLoadLineUps();
            }
            createMappings();
            setTeamLineUpActive();
            updateDisplay();
            init = false;
        }
        else
        {
            createMappings();
            setTeamLineUpActive();
            updateDisplay();
            saveAllData();
        }
    }

    private void LoadFileDialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", MODE_PRIVATE);
        String defaultDir = settings.getString("defaultDir", "/sdcard");
        properties.root = new File("/sdcard");
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(defaultDir);
        properties.extensions = null;
        FilePickerDialog dialog = new FilePickerDialog(MainActivity.this, properties);
        dialog.setTitle("Load File...");

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if(files.length == 1)
                {
                    File loadFile = new File(files[0]);
                    MainActivity.data.paddlers.clear();
                    MainActivity.data.uberLoadPaddlers(loadFile);
                    MainActivity.thisActivity.setTeamLineUpActive();

                    SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    String defaultDir = loadFile.getParent();
                    //Log.d("FILE SAVING", defaultDir);
                    editor.putString("defaultDir", defaultDir);
                    editor.apply();

                }
            }
        });
        dialog.show();
    }

    private void SaveFileDialog(final boolean followWithLoad) {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_AND_DIR_SELECT;
        SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", MODE_PRIVATE);
        String defaultDir = settings.getString("defaultDir", "/sdcard");
        //String defaultDir = "/sdcard";
        properties.root = new File("/sdcard");
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(defaultDir);
        properties.extensions = null;
        FilePickerDialog dialog = new FilePickerDialog(MainActivity.this, properties);
        dialog.setTitle("Save File...");

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.

                if(files.length == 1)
                {
                    final String dirName = files[0];
                    File dirFile = new File(dirName);
                    String newFileName = "newClubStore.csv";
                    if(dirFile.isFile())
                    {
                        newFileName = dirFile.getName();
                        dirFile = dirFile.getParentFile();
                    }
                    final String finalDir = dirFile.getPath();
                    if(dirFile.isDirectory())
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Change LineUp Name");

                        // Set up the input
                        final EditText input = new EditText(MainActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        input.setText(newFileName);
                        builder.setView(input);

// Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String filename = input.getText().toString();
                                Log.d("FILE SAVING", finalDir + "/" + filename);
                                File saveFile = new File(finalDir + "/" + filename);
                                MainActivity.data.uberSavePaddlers(saveFile);
                                if(followWithLoad == true)
                                {
                                    LoadFileDialog();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                    else if(dirFile.isFile())
                    {
                        // Add a caution here for overwriting?
                        MainActivity.data.uberSavePaddlers(dirFile);
                    }

                }
            }
        });
        dialog.show();
    }

    public static void saveAllData()
    {
        File importDir = new File(Environment.getExternalStorageDirectory(), "");
        File file = new File(importDir, "MasterPaddlers.csv");
        data.uberSavePaddlers(file);
        //data.savePaddlers();
        //saveAllLineUps();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTeamLineUpActive();
        updateDisplay();
    }

    private void CheckSetPermissions() {
        int writeCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int internetCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int phoneCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        int coarseCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(writeCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
        if(readCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE);
        }
        if(internetCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        }
        if(phoneCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE);
        }
        if(coarseCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CORSE_LOCATION);
        }
        if(fineCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
        Log.d("Permissions Check Write", String.valueOf(writeCheck));
        Log.d("Permissions Check Read", String.valueOf(readCheck));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public double getPositionWeight(int position)
    {
        PaddlerData paddler = data.getPaddler(teamLineUps[currTeam].getPaddlerInLineUp(position, currLineUp));
        //paddlerLookup.get(paddlerPositions[position]);
        if(paddler != null)
        {
            return paddler.weight;
        }
        return 0.0d;
    }

    public double calculateFrontLeftWeight()
    {
        double total = 0.0d;
        int startPos = 0;
        for(int i = 0; i < 5; i++)
        {
            total += getPositionWeight(startPos);
            startPos+=2;
        }
        return total;
    }

    public double calculateFrontRightWeight()
    {
        double total = 0.0d;
        int startPos = 1;
        for(int i = 0; i < 5; i++)
        {
            total += getPositionWeight(startPos);
            startPos+=2;
        }
        return total;
    }

    public double calculateBackLeftWeight()
    {
        double total = 0.0d;
        int startPos = 10;
        for(int i = 0; i < 5; i++)
        {
            total += getPositionWeight(startPos);
            startPos+=2;
        }
        return total;
    }

    public double calculateBackRightWeight()
    {
        double total = 0.0d;
        int startPos = 11;
        for(int i = 0; i < 5; i++)
        {
            total += getPositionWeight(startPos);
            startPos+=2;
        }
        return total;
    }

    public double calculateFrontWeight()
    {
        double total = 0.0d;
        int startPos = 0;
        for(int i = 0; i < 10; i++)
        {
            total += getPositionWeight(startPos);
            startPos+=1;
        }
        // add the drummer
        total += getPositionWeight(20);
        return total;
    }

    public double calculateBackWeight()
    {
        double total = 0.0d;
        int startPos = 10;
        for(int i = 0; i < 10; i++)
        {
            total += getPositionWeight(startPos);
            startPos+=1;
        }
        // add the tiller
        total += getPositionWeight(21);
        return total;
    }

    public void updateWeights()
    {
        double frontLeft = calculateFrontLeftWeight();
        double frontRight = calculateFrontRightWeight();
        double backLeft = calculateBackLeftWeight();
        double backRight = calculateBackRightWeight();
        double front = calculateFrontWeight();
        double back = calculateBackWeight();
        double left = frontLeft + backLeft;
        double right = frontRight + backRight;

        double lrfactor = Math.min(1.0, 10.0 * Math.abs((left-right) / (left+right)));
        double fbfactor = Math.min(1.0, 10.0 * Math.abs((front-back) / (front+back)));

        int redIntensity = 255 - (int)(255d * lrfactor);
        leftField.setBackgroundColor(Color.argb(255, 255, redIntensity, redIntensity));
        rightField.setBackgroundColor(Color.argb(255, 255, redIntensity, redIntensity));

        redIntensity = 255 - (int)(255d * fbfactor);
        frontField.setBackgroundColor(Color.argb(255, 255, redIntensity, redIntensity));
        backField.setBackgroundColor(Color.argb(255, 255, redIntensity, redIntensity));

        frontLeftField.setText(String.format("%.0f",frontLeft));
        frontRightField.setText(String.format("%.0f",frontRight));
        backLeftField.setText(String.format("%.0f",backLeft));
        backRightField.setText(String.format("%.0f",backRight));
        frontField.setText(String.format("%.0f",front));
        backField.setText(String.format("%.0f",back));
        leftField.setText(String.format("%.0f",left));
        rightField.setText(String.format("%.0f",right));

    }

    @Override
    public void onBackPressed() {
        // do nothing. We want to force user to stay in this activity and not drop out.
    }

    public void updateDisplay()
    {
        clearColors();
        colorEmptySeats();
        setInBoatColors();
        markDuplicates();
        updateWeights();

    }

    public void colorEmptySeats()
    {
        for(int i = 0; i < crewSize; i++)
        {
            if(teamLineUps[currTeam].getPaddlerInLineUp(i, currLineUp) < 0)
            {
                paddlerPositionsDisplay[i].setBackgroundColor(Color.LTGRAY);
            }
        }
    }
    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction()==DragEvent.ACTION_DROP)
        {
            //handle the dragged view being dropped over a target view
            TextView dropped = (TextView)event.getLocalState();
            TextView dropTarget = (TextView) v;
            //stop displaying the view where it was before it was dragged
            //dropped.setVisibility(View.INVISIBLE);

            int index = positionLookup.get(dropTarget.getId());
            PaddlerData paddler = paddlerLookup.get(dropped.getId());
            if(paddler != null)
            {
                dropTarget.setText(paddler.shortName + " | " + paddler.side );
                teamLineUps[currTeam].setPaddlerInLineUp(paddler.id, index, currLineUp);
                //paddlerPositions[index] = dropped.getId();
                paddlerWeightDisplay[index].setText(Double.toString(paddler.weight));
            }
            else
            {
                // In the case of a null paddler, check if we were dragging another seat position
                int droppedId = dropped.getId();
                if(positionLookup.containsKey(droppedId))
                {
                    int sourceIndex = positionLookup.get(droppedId);
                    // we can just swap the allocations?
                    int paddlerIndexA = teamLineUps[currTeam].getPaddlerInLineUp(index, currLineUp);
                    int paddlerIndexB = teamLineUps[currTeam].getPaddlerInLineUp(sourceIndex, currLineUp);
                    teamLineUps[currTeam].setPaddlerInLineUp(paddlerIndexB, index, currLineUp);
                    teamLineUps[currTeam].setPaddlerInLineUp(paddlerIndexA, sourceIndex, currLineUp);
                    PaddlerData paddlerA = data.getPaddler(paddlerIndexA);
                    PaddlerData paddlerB = data.getPaddler(paddlerIndexB);
                    // paddlerA is going to the sourceIndex
                    // paddlerB is going to the index
                    if(paddlerA != null)
                    {
                        paddlerPositionsDisplay[sourceIndex].setText(paddlerA.shortName + " | " + paddlerA.side );
                        paddlerWeightDisplay[sourceIndex].setText(Double.toString(paddlerA.weight));
                    }
                    else
                    {
                        paddlerPositionsDisplay[sourceIndex].setText("Empty" );
                        paddlerWeightDisplay[sourceIndex].setText("-");
                    }
                    if(paddlerB != null)
                    {
                        paddlerPositionsDisplay[index].setText(paddlerB.shortName + " | " + paddlerB.side );
                        paddlerWeightDisplay[index].setText(Double.toString(paddlerB.weight));
                    }
                    else
                    {
                        paddlerPositionsDisplay[index].setText("Empty" );
                        paddlerWeightDisplay[index].setText("-");
                    }
                }
                else
                {
                    dropTarget.setText("Empty");
                    //paddlerPositions[index] = 0;
                    teamLineUps[currTeam].setPaddlerInLineUp(-1, index, currLineUp);
                    paddlerWeightDisplay[index].setText("-");
                }
            }
            updateDisplay();
        }
        else if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED)
        {
            TextView dropTarget = (TextView) v;
            updateDisplay();
            dropTarget.setBackgroundColor(Color.CYAN);
        }
        else if(event.getAction() == DragEvent.ACTION_DRAG_EXITED)
        {
            updateDisplay();
        }
        return true;
    }


    public void clearColors()
    {
        for(int i = 0; i < teamSize; i++)
        {
            paddlers[i].setBackgroundColor(Color.argb(255, 255, 255, 255));
        }
        for(int i = 0; i < crewSize; i++)
        {
            paddlerPositionsDisplay[i].setBackgroundColor(Color.argb(255, 255, 255, 255));
        }
    }

    public void setInBoatColors()
    {
        int colorMale = getResources().getColor(R.color.colorMale);
        int colorFemale = getResources().getColor(R.color.colorFemale);
        for(int i = 0; i < teamSize; i++)
        {
            //TextView paddlerView = paddlerPositionsDisplay[i];
            PaddlerData paddler = null;
            if (paddlerLookup.containsKey(paddlers[i].getId()))
            {
                paddler = paddlerLookup.get(paddlers[i].getId());
            }

            if(paddler != null )
            {
                if(paddler.female)
                {
                    paddlers[i].setBackgroundColor(colorFemale);
                }
                else
                {
                    paddlers[i].setBackgroundColor(colorMale);
                }
            }
        }

        for(int i = 0; i < crewSize; i++)
        {
            int paddlerId = teamLineUps[currTeam].getPaddlerInLineUp(i, currLineUp);
            if(paddlerId >= 0)
            {
                PaddlerData paddler = data.getPaddler(paddlerId);
                int index = teamLookup.get(paddlerId);
                if(paddler.female)
                {
                    paddlerPositionsDisplay[i].setBackgroundColor(colorFemale);
                    paddlers[index].setBackgroundColor(Color.argb(255, 70, 170, 70));
                }
                else
                {
                    paddlerPositionsDisplay[i].setBackgroundColor(colorMale);
                    paddlers[index].setBackgroundColor(Color.argb(255, 70, 170, 70));
                }

            }
        }
    }


    public void markDuplicates()
    {
        // check that no paddler is in the mix more than once
        for(int i = 0; i < crewSize; i++)
        {
            int paddlerA = teamLineUps[currTeam].getPaddlerInLineUp(i, currLineUp);
            for(int j= i +1; j < crewSize; j++)
            {
                int paddlerB = teamLineUps[currTeam].getPaddlerInLineUp(j, currLineUp);
                if(paddlerA == paddlerB && paddlerB != -1)
                {
                    paddlerPositionsDisplay[i].setBackgroundColor(Color.RED);
                    paddlerPositionsDisplay[j].setBackgroundColor(Color.RED);
                    int index = teamLookup.get(paddlerA);
                    paddlers[index].setBackgroundColor(Color.RED);
                }
            }
        }
    }

    public void markUnused()
    {
        // check that no paddler is in the mix more than once

    }
    //When text1 or text2 or text3 gets clicked or touched then this method will be called
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN)
        {
            //int allign = v.getTextAlignment();
            //v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

            ClipData data = ClipData.newPlainText("", "");
            v.startDrag(data, shadowBuilder, v, 0);
            //v.setTextAlignment(allign);
            return true;
        }
        else return false;
    }

    @Override
    public void onClick(View view) {

    }
}
