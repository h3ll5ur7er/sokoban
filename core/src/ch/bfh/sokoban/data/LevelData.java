//*********************************************************//
// Author: Käser Robin, Knecht Emanuel                     //
// Berner Fachhochschule                                   //
//*********************************************************//

package ch.bfh.sokoban.data;

import java.util.UUID;

/**
 * Structure to put LevelData in
 */
public class LevelData
{
    public String id, name;
    public int width, height;
    public String[] data;

    public LevelData()
    {
        id  = UUID.randomUUID().toString();
    }

    public LevelData(String name, String id, int width, int height, String...data)
    {
        this.id = id != ""?id:UUID.randomUUID().toString();
        this.name = name;
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public LevelData(String name, int width, int height, String...data)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.width = width;
        this.height = height;
        this.data = data;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
