package us.blandfamily.dbal;

import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Lachlan on 8/28/2016.
 */
public class TeamLineUps {

    static private int crewSize = 22;
    final private int numLineUps = 10;
    int[] paddlerPositions = new int[crewSize * 10];
    int teamIndex;
    String lineUpsNames[] = new String[10];
    String teamName;

    public TeamLineUps(int teamIndex)
    {
        this.teamIndex = teamIndex;
        for(int i = 0; i < numLineUps; i++)
        {
            clearLineUp(i);
        }
    }

    public void saveLineUps()
    {
        String filename = "lineUps" + String.valueOf(teamIndex) + ".csv";
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        Log.d("Saving LineUps", exportDir + ": ");
        File file = new File(exportDir, filename);
        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            for(int i = 0; i < numLineUps; i++) {
                String arrStr[] = new String[crewSize+1];
                for(int crewIndex = 0; crewIndex < crewSize; crewIndex++)
                {
                    arrStr[crewIndex] = String.valueOf(getPaddlerInLineUp(crewIndex, i));
                }
                arrStr[crewSize] = lineUpsNames[i];
                csvWrite.writeNext(arrStr);
            }
            String arrStr[] = new String[1];
            arrStr[0] = teamName;
            csvWrite.writeNext(arrStr);
            csvWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadLineUps()
    {
        String filename = "lineUps" + String.valueOf(teamIndex) + ".csv";
        File importDir = new File(Environment.getExternalStorageDirectory(), "");
        File file = new File(importDir, filename);
        Log.d("Loading LineUps", importDir + ": ");
        int numRead = 0;
        try {
            CSVReader csvRead = new CSVReader(new FileReader(file));

            String arrStr[];
            while((arrStr = csvRead.readNext())!= null && numRead < numLineUps)
            {
                for(int i = 0; i < crewSize && i < arrStr.length; i++)
                {
                    setPaddlerInLineUp(Integer.parseInt(arrStr[i]), i, numRead);
                }
                if(arrStr.length > crewSize)
                {
                    lineUpsNames[numRead] = arrStr[crewSize];
                }
                else
                {
                    lineUpsNames[numRead] = String.format("LineUp. %d", numRead +1);
                }
                numRead++;
            }
            if(arrStr != null)
            {
                teamName = arrStr[0];
            }
            else
            {
                teamName = "Team " + String.valueOf(teamIndex+1);
            }
            csvRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Reading LineUps", Integer.toString(numRead));
        return numRead> 0;
    }

    public void clearLineUp(int lineUpIndex)
    {
        for(int i = 0; i < crewSize; i++)
        {
            paddlerPositions[lineUpIndex * crewSize + i] = -1;
        }
        lineUpsNames[lineUpIndex] = String.format("LineUp %d", lineUpIndex +1);
    }

    public void copyLineUp(int src, int dest)
    {
        for(int i = 0; i < crewSize; i++)
        {
            paddlerPositions[dest * crewSize + i] = paddlerPositions[src * crewSize + i];
        }
    }

    public void setPaddlerInLineUp(int paddlerID, int position, int lineUp)
    {
        paddlerPositions[lineUp * crewSize + position] = paddlerID;
    }

    public int getPaddlerInLineUp(int position, int lineUp)
    {
        return paddlerPositions[lineUp * crewSize + position];
    }

    public String getLineUpName(int lineUp)
    {
        return lineUpsNames[lineUp];
    }

    public void setLineUpName(int lineUp, String name)
    {
        lineUpsNames[lineUp] = name;
    }

    public void verifyAndSanitizeLineUp(ClubDataStore data)
    {
        for(int i = 0; i < paddlerPositions.length; i++)
        {
            if(paddlerPositions[i] >= 0)
            {
                PaddlerData paddler = data.getPaddler(paddlerPositions[i]);
                if(paddler != null)
                {
                    if(paddler.teams[teamIndex] != true)
                    {
                        paddlerPositions[i] = -1;
                    }
                }
                else
                {
                    // paddler can't be found
                    paddlerPositions[i] = -1;
                }
            }
        }
    }

}
