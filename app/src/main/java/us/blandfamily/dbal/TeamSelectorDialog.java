package us.blandfamily.dbal;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lachlan on 8/29/2016.
 */
public class TeamSelectorDialog {
    static int selection = -1;
    static void launchTeamSelectorDialog(final MainActivity launchingActivity)
    {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(launchingActivity);

        String teamNames[] = new String[6];
        for(int i = 0; i < 6; i++)
        {
            teamNames[i] = MainActivity.teamLineUps[i].teamName;
        }
        builder.setTitle(R.string.select_team)
        .setSingleChoiceItems(teamNames, MainActivity.currTeam,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if (isChecked) {
                        // If the user checked the item, add it to the selected items
                        MainActivity.nextTeam = which;
                    //}
                }
            })
        // Add the buttons
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                MainActivity.currTeam = MainActivity.nextTeam;
                launchingActivity.setTeamLineUpActive();
                launchingActivity.saveAllData();
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    static void launchTeamSelectorDialog(final team_paddlers launchingActivity)
    {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(launchingActivity);

        String teamNames[] = new String[6];
        for(int i = 0; i < 6; i++)
        {
            teamNames[i] = MainActivity.teamLineUps[i].teamName;
        }
        builder.setTitle(R.string.select_team)
                .setSingleChoiceItems(teamNames, MainActivity.currTeam,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                MainActivity.nextTeam = which;
                                //}
                            }
                        })
                // Add the buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        MainActivity.currTeam = MainActivity.nextTeam;
                        MainActivity.thisActivity.setTeamLineUpActive();
                        MainActivity.thisActivity.saveAllData();
                        launchingActivity.refresh();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    static void launchLineUpSelectorDialog(final AppCompatActivity launchingActrivity)
    {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(launchingActrivity);
        //String lineUpNames[] = new String[10];
        String lineUpNames[] = MainActivity.teamLineUps[MainActivity.currTeam].lineUpsNames;

        builder.setTitle(R.string.select_lineup)
                .setSingleChoiceItems(lineUpNames, MainActivity.currLineUp,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                MainActivity.nextLineUp = which;
                                //}
                            }
                        })
                // Add the buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        MainActivity.currLineUp = MainActivity.nextLineUp;
                        MainActivity.thisActivity.setTeamLineUpActive();
                        MainActivity.thisActivity.saveAllData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    static void launchLineUpCopyDialog(final AppCompatActivity launchingActrivity)
    {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(launchingActrivity);
        //String lineUpNames[] = new String[10];
        String lineUpNames[] = MainActivity.teamLineUps[MainActivity.currTeam].lineUpsNames;
        selection = MainActivity.currLineUp;
        builder.setTitle(R.string.copy_lineup)
                .setSingleChoiceItems(lineUpNames, MainActivity.currLineUp,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                selection = which;
                                //}
                            }
                        })
                // Add the buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        if(selection == MainActivity.currLineUp)
                        {
                            // toast
                            Toast.makeText(launchingActrivity, "Can't copy onto self", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(launchingActrivity, "Copying from " + MainActivity.teamLineUps[MainActivity.currTeam].lineUpsNames[MainActivity.currLineUp] + " to " + MainActivity.teamLineUps[MainActivity.currTeam].lineUpsNames[selection], Toast.LENGTH_SHORT).show();
                            MainActivity.teamLineUps[MainActivity.currTeam].copyLineUp(MainActivity.currLineUp, selection);
                        }
                        //MainActivity.currLineUp = MainActivity.nextLineUp;
                        //launchingActrivity.setTeamLineUpActive();
                        //launchingActrivity.saveAllData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    static void launchSaveDialog(final AppCompatActivity launchingActrivity)
    {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(launchingActrivity);
        builder.setTitle(R.string.save_title);
        builder.setMessage(R.string.save_message)
                // Add the buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        MainActivity.saveAllData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    static void lineUpNameEdit(final AppCompatActivity launchingActrivity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(launchingActrivity);
        builder.setTitle("Change LineUp Name");

        // Set up the input
        final EditText input = new EditText(launchingActrivity);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(MainActivity.teamLineUps[MainActivity.currTeam].lineUpsNames[MainActivity.currLineUp]);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.teamLineUps[MainActivity.currTeam].lineUpsNames[MainActivity.currLineUp] = input.getText().toString();
                MainActivity.thisActivity.lineUpDisplay.setText(input.getText().toString());
                MainActivity.thisActivity.saveAllData();
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

    static void teamNameEdit(final AppCompatActivity launchingActrivity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(launchingActrivity);
        builder.setTitle("Change Team Name");

        // Set up the input
        final EditText input = new EditText(launchingActrivity);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(MainActivity.teamLineUps[MainActivity.currTeam].teamName);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.teamLineUps[MainActivity.currTeam].teamName = input.getText().toString();
                MainActivity.thisActivity.teamDisplay.setText(input.getText().toString());
                MainActivity.thisActivity.saveAllData();
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
}
