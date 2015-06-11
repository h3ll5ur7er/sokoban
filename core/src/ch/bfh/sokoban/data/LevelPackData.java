//*********************************************************//
// Author: Käser Robin, Knecht Emanuel                     //
// Berner Fachhochschule                                   //
//*********************************************************//

package ch.bfh.sokoban.data;

import java.util.UUID;

/**
 * Structure to put LevelPack Data inside
 */
public class LevelPackData
{
    public String name, description, email, url, copyright,identifier;
    public LevelData[] levels;

    public LevelPackData()
    {
        this.identifier = UUID.randomUUID().toString();
    }

    public LevelPackData(String id, String name, String description, String email, String url, String copyright, LevelData...levels)
    {
        this.identifier = id != ""?id:UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.email = email;
        this.url = url;
        this.copyright = copyright;
        this.levels = levels;
    }

    public LevelPackData(String name, String description, String email, String url, String copyright, LevelData...levels)
    {
        this.identifier = UUID.randomUUID().toString();
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
