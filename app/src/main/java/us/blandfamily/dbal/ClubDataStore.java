package us.blandfamily.dbal;

import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lachlan on 8/21/2016.
 */
public class ClubDataStore {
    public HashMap<Integer, PaddlerData> paddlers = new HashMap<Integer, PaddlerData>();

    public boolean loadPaddlers()
    {
        File importDir = new File(Environment.getExternalStorageDirectory(), "");
        File file = new File(importDir, "paddlers.csv");
        Log.d("Loading Paddlers", importDir + ": ");
        int numRead = 0;
        try {
            CSVReader csvRead = new CSVReader(new FileReader(file));

            String arrStr[];
            while((arrStr = csvRead.readNext())!= null)
            {
                int id = Integer.parseInt(arrStr[0]);
                double weight = Double.parseDouble(arrStr[4]);
                boolean isFemale = false;
                if(arrStr.length > 11)
                {
                    isFemale = Boolean.valueOf(arrStr[11]);
                }
                PaddlerData newPaddler = new PaddlerData(id, arrStr[1], arrStr[2], arrStr[3], weight, buildTeamsArray(arrStr), isFemale);
                addPaddler(newPaddler);
                numRead++;
            }
            csvRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Reading Paddlers", Integer.toString(numRead));
        return numRead > 0;
    }

    private boolean[] buildTeamsArray(String[] lineInput)
    {
        boolean[] retVal = new boolean[6];
        for(int i = 0; i < 6; i++)
        {
            retVal[i] = false;
        }
        // team data starts coming in from array element 5 onwards
        int startPoint = 5;
        for(int i = 0; i < 6; i++)
        {
            int targetPos = i + startPoint;
            if(targetPos >= lineInput.length)
            {
                continue;
            }
            retVal[i] = Boolean.valueOf(lineInput[targetPos]);
        }
        return retVal;
    }
    public void savePaddlers()
    {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        Log.d("Saving PaddlersX", exportDir + ": ");
        File file = new File(exportDir, "paddlers.csv");
        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            for(PaddlerData paddler : paddlers.values()) {

                String arrStr[] = {String.valueOf(paddler.id), paddler.name, paddler.shortName, paddler.side, String.valueOf(paddler.weight),
                String.valueOf(paddler.teams[0]), String.valueOf(paddler.teams[1]), String.valueOf(paddler.teams[2]),
                        String.valueOf(paddler.teams[3]), String.valueOf(paddler.teams[4]), String.valueOf(paddler.teams[5]), String.valueOf(paddler.female)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addPaddler(PaddlerData newPaddler)
    {
        if(paddlers.containsKey(newPaddler.id))
        {
            return false; // can add a paddler if the id already exists
        }
        paddlers.put(newPaddler.id, newPaddler);
        return true;
    }

    public PaddlerData getPaddler(int id)
    {
        return paddlers.get(id);
    }

    public int getUnusedPaddlerId()
    {
        for(int i = 0; i < 1000000; i++)
        {
            if(getPaddler(i) == null)
            {
                return i;
            }
        }
        return -1;
    }
}
