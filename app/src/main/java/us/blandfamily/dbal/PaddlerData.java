package us.blandfamily.dbal;

/**
 * Created by Lachlan on 8/18/2016.
 */
public class PaddlerData implements Comparable{
    public int id;
    public String name;
    public String shortName;
    public String side;
    public double weight;
    public boolean teams[] = new boolean[6];
    public boolean female = false;

    public PaddlerData(int newId, String newName, String newShortName, String newSide, double newWeight)
    {
        id = newId;
        name = newName;
        shortName = newShortName;
        side = newSide;
        weight = newWeight;
        for(int i = 0; i < 6; i++)
        {
            teams[i] = false;
        }
    }

    public PaddlerData(int newId, String newName, String newShortName, String newSide, double newWeight, boolean newTeams[], boolean newFemale)
    {
        id = newId;
        name = newName;
        shortName = newShortName;
        side = newSide;
        weight = newWeight;
        for(int i = 0; i < 6; i++)
        {
            teams[i] = newTeams[i];
        }
        female = newFemale;
    }

    @Override
    public int compareTo(Object o) {
        PaddlerData other = (PaddlerData) o;
        return name.compareTo(other.name);
    }
}
