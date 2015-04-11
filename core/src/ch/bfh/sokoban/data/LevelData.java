package ch.bfh.sokoban.data;

/**
 * Structure to put LevelData in
 */
public class LevelData
{
    public String id;
    public int width, height;
    public String[] data;

    public LevelData()
    {
    }

    public LevelData(String name, int width, int height, String...data)
    {
        this.id = name;
        this.width = width;
        this.height = height;
        this.data = data;
    }

    @Override
    public String toString()
    {
        return id;
    }
}
