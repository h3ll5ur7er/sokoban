package ch.bfh.sokoban.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Root object of the LevelCollection
 */
public class LevelPackDataCollection
{
    public List<LevelPackData> levelPacks;

    public LevelPackDataCollection()
    {
    }

    public LevelPackDataCollection(LevelPackData... levelpacks)
    {
        this.levelPacks = new ArrayList<LevelPackData>();
        Collections.addAll(this.levelPacks, levelpacks);

    }
}
