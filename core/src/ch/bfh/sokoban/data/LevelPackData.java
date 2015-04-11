package ch.bfh.sokoban.data;

/**
 * Structure to put LevelPack Data inside
 */
public class LevelPackData
{
    public String name, description, email, url, copyright;
    public LevelData[] levels;

    public LevelPackData()
    {
    }

    public LevelPackData(String name, String description, String email, String url, String copyright, LevelData...levels)
    {
        this.name = name;
        this.description = description;
        this.email = email;
        this.url = url;
        this.copyright = copyright;
        this.levels = levels;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
