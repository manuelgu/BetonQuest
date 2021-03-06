/**
 * BetonQuest - advanced quests for Bukkit
 * Copyright (C) 2015  Jakub "Co0sh" Sapalski
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.betoncraft.betonquest.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pl.betoncraft.betonquest.BetonQuest;

/**
 * Connects to the database and queries it
 * 
 * @author Jakub Sapalski
 */
public class Connector {
    
    private BetonQuest plugin;
    private String prefix;
    private Database db;
    private Connection connection;

    /**
     * Opens a new connection to the database
     */
    public Connector() {
        plugin = BetonQuest.getInstance();
        prefix = plugin.getConfig().getString("mysql.prefix", "");
        db = plugin.getDB();
        connection = db.getConnection();
    }
    
    /**
     * Queries the database with the given type and arguments
     * 
     * @param type
     *          type of the query
     * @param args
     *          arguments
     * @return ResultSet with the requsted data
     */
    public ResultSet querySQL(QueryType type, String[] args) {
        try {
            PreparedStatement statement;
            switch (type) {
                case SELECT_JOURNAL:
                    statement = connection.prepareStatement("SELECT pointer, date FROM " + prefix + "journal WHERE playerID = ?;");
                    break;
                case SELECT_POINTS:
                    statement = connection.prepareStatement("SELECT category, count FROM " + prefix + "points WHERE playerID = ?;");
                    break;
                case SELECT_OBJECTIVES:
                    statement = connection.prepareStatement("SELECT objective, instructions FROM " + prefix + "objectives WHERE playerID = ?;");
                    break;
                case SELECT_TAGS:
                    statement = connection.prepareStatement("SELECT tag FROM " + prefix + "tags WHERE playerID = ?;");
                    break;
                case SELECT_BACKPACK:
                    statement = connection.prepareStatement("SELECT instruction, amount FROM " + prefix + "backpack WHERE playerID = ?;");
                    break;
                case SELECT_PLAYER:
                    statement = connection.prepareStatement("SELECT language, conversation FROM " + prefix + "player WHERE playerID = ?;");
                    break;
                case SELECT_PLAYERS_TAGS:
                    statement = connection.prepareStatement("SELECT playerID FROM " + prefix + "tags GROUP BY playerID;");
                    break;
                case SELECT_PLAYERS_JOURNAL:
                    statement = connection.prepareStatement("SELECT playerID FROM " + prefix + "journal GROUP BY playerID;");
                    break;
                case SELECT_PLAYERS_POINTS:
                    statement = connection.prepareStatement("SELECT playerID FROM " + prefix + "points GROUP BY playerID;");
                    break;
                case SELECT_PLAYERS_OBJECTIVES:
                    statement = connection.prepareStatement("SELECT playerID FROM " + prefix + "objectives GROUP BY playerID;");
                    break;
                case SELECT_PLAYERS_BACKPACK:
                    statement = connection.prepareStatement("SELECT playerID FROM " + prefix + "backpack GROUP BY playerID;");
                    break;
                case LOAD_ALL_JOURNALS:
                    statement = connection.prepareStatement("SELECT * FROM " + prefix + "journal");
                    break;
                case LOAD_ALL_OBJECTIVES:
                    statement = connection.prepareStatement("SELECT * FROM " + prefix + "objectives");
                    break;
                case LOAD_ALL_POINTS:
                    statement = connection.prepareStatement("SELECT * FROM " + prefix + "points");
                    break;
                case LOAD_ALL_TAGS:
                    statement = connection.prepareStatement("SELECT * FROM " + prefix + "tags");
                    break;
                case LOAD_ALL_BACKPACK:
                    statement = connection.prepareStatement("SELECT * FROM " + prefix + "backpack");
                    break;
                case LOAD_ALL_PLAYER:
                    statement = connection.prepareStatement("SELECT * FROM " + prefix + "player");
                    break;
                default:
                    statement = null;
                    break;
            }
            for (int i = 0; i < args.length; i++) {
                statement.setString(i + 1, args[i]);
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the database with the given type and arguments
     * 
     * @param type
     *          type of the update
     * @param args
     *          arguments
     */
    public void updateSQL(UpdateType type, String[] args) {
        try {
            PreparedStatement statement;
            switch (type) {
                case ADD_OBJECTIVES:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "objectives (playerID, objective, instructions) VALUES (?, ?, ?);");
                    break;
                case ADD_TAGS:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "tags (playerID, tag) VALUES (?, ?);");
                    break;
                case ADD_POINTS:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "points (playerID, category, count) VALUES (?, ?, ?);");
                    break;
                case ADD_JOURNAL:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "journal (playerID, pointer, date) VALUES (?, ?, ?);");
                    break;
                case ADD_BACKPACK:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "backpack (playerID, instruction, amount) VALUES (?, ?, ?);");
                    break;
                case ADD_PLAYER:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "player (playerID, language) VALUES (?, ?);");
                    break;
                case REMOVE_OBJECTIVES:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "objectives WHERE playerID = ? AND objective = ?;");
                    break;
                case REMOVE_TAGS:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "tags WHERE playerID = ? AND tag = ?;");
                    break;
                case REMOVE_POINTS:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "points WHERE playerID = ? AND category = ?;");
                    break;
                case REMOVE_JOURNAL:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "journal WHERE playerID = ? AND pointer = ? AND date = ?;");
                    break;
                case DELETE_OBJECTIVES:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "objectives WHERE playerID = ?;");
                    break;
                case DELETE_TAGS:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "tags WHERE playerID = ?;");
                    break;
                case DELETE_POINTS:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "points WHERE playerID = ?;");
                    break;
                case DELETE_JOURNAL:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "journal WHERE playerID = ?;");
                    break;
                case DELETE_BACKPACK:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "backpack WHERE playerID = ?;");
                    break;
                case DELETE_PLAYER:
                    statement = connection.prepareStatement("DELETE FROM " + prefix + "player WHERE playerID = ?;");
                    break;
                case UPDATE_PLAYERS_OBJECTIVES:
                    statement = connection.prepareStatement("UPDATE " + prefix + "objectives SET playerID = ? WHERE playerID = ?;");
                    break;
                case UPDATE_PLAYERS_TAGS:
                    statement = connection.prepareStatement("UPDATE " + prefix + "tags SET playerID = ? WHERE playerID = ?;");
                    break;
                case UPDATE_PLAYERS_POINTS:
                    statement = connection.prepareStatement("UPDATE " + prefix + "points SET playerID = ? WHERE playerID = ?;");
                    break;
                case UPDATE_PLAYERS_JOURNAL:
                    statement = connection.prepareStatement("UPDATE " + prefix + "journal SET playerID = ? WHERE playerID = ?;");
                    break;
                case UPDATE_PLAYERS_BACKPACK:
                    statement = connection.prepareStatement("UPDATE " + prefix + "backpack SET playerID = ? WHERE playerID = ?;");
                    break;
                case DROP_OBJECTIVES:
                    statement = connection.prepareStatement("DROP TABLE " + prefix + "objectives");
                    break;
                case DROP_TAGS:
                    statement = connection.prepareStatement("DROP TABLE " + prefix + "tags");
                    break;
                case DROP_POINTS:
                    statement = connection.prepareStatement("DROP TABLE " + prefix + "points");
                    break;
                case DROP_JOURNALS:
                    statement = connection.prepareStatement("DROP TABLE " + prefix + "journal");
                    break;
                case DROP_BACKPACK:
                    statement = connection.prepareStatement("DROP TABLE " + prefix + "backpack");
                    break;
                case DROP_PLAYER:
                    statement = connection.prepareStatement("DROP TABLE " + prefix + "player");
                    break;
                case INSERT_OBJECTIVE:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "objectives VALUES (?,?,?,?)");
                    break;
                case INSERT_TAG:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "tags VALUES (?,?,?)");
                    break;
                case INSERT_POINT:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "points VALUES (?,?,?,?)");
                    break;
                case INSERT_JOURNAL:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "journal VALUES (?,?,?,?)");
                    break;
                case INSERT_BACKPACK:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "backpack VALUES (?,?,?,?)");
                    break;
                case INSERT_PLAYER:
                    statement = connection.prepareStatement("INSERT INTO " + prefix + "player VALUES (?,?,?,?);");
                    break;
                case UPDATE_CONVERSATION:
                    statement = connection.prepareStatement("UPDATE " + prefix + "player SET conversation = ? WHERE playerID = ?");
                    break;
                default:
                    statement = null;
                    break;
            }
            for (int i = 0; i < args.length; i++) {
                statement.setString(i + 1, args[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Type of the query
     */
    public enum QueryType {

        SELECT_OBJECTIVES, SELECT_TAGS, SELECT_POINTS, SELECT_JOURNAL, SELECT_BACKPACK, SELECT_PLAYER,

        SELECT_PLAYERS_TAGS, SELECT_PLAYERS_JOURNAL, SELECT_PLAYERS_POINTS,
        SELECT_PLAYERS_OBJECTIVES, SELECT_PLAYERS_BACKPACK,
        
        LOAD_ALL_OBJECTIVES, LOAD_ALL_TAGS, LOAD_ALL_POINTS, LOAD_ALL_JOURNALS,
        LOAD_ALL_BACKPACK, LOAD_ALL_PLAYER
    }

    /**
     * Type of the update
     */
    public enum UpdateType {

        /**
         * Add the single objective to the database. PlayerID, objectiveID, instruction.
         */
        ADD_OBJECTIVES,
        /**
         * Add the single tag to the database. PlayerID, tag.
         */
        ADD_TAGS,
        /**
         * Add single point category to the database. PlayerID, category, amount.
         */
        ADD_POINTS,
        /**
         * Add single journal entry to the database. PlayerID, pointer, date.
         */
        ADD_JOURNAL,
        /**
         * Add single itemstack to the database. PlayerID, instruction, amount.
         */
        ADD_BACKPACK,
        /**
         * Add single player to the database. PlayerID, language.
         */
        ADD_PLAYER,
        /**
         * Removes the single objective from the database. PlayerID, objectiveID.
         */
        REMOVE_OBJECTIVES,
        /**
         * Removes the single tag from the database. PlayerID, tag.
         */
        REMOVE_TAGS,
        /**
         * Removes single point category from the database. PlayerID, category.
         */
        REMOVE_POINTS,
        /**
         * Removes single journal entry from the database. PlayerID, pointer, date.
         */
        REMOVE_JOURNAL,
        
        DELETE_OBJECTIVES, DELETE_TAGS, DELETE_POINTS, DELETE_JOURNAL, DELETE_BACKPACK, DELETE_PLAYER,

        UPDATE_PLAYERS_OBJECTIVES, UPDATE_PLAYERS_TAGS, UPDATE_PLAYERS_POINTS,
        UPDATE_PLAYERS_JOURNAL, UPDATE_PLAYERS_BACKPACK,
        
        DROP_OBJECTIVES, DROP_TAGS, DROP_POINTS, DROP_JOURNALS, DROP_BACKPACK, DROP_PLAYER,
        
        INSERT_OBJECTIVE, INSERT_TAG, INSERT_POINT, INSERT_JOURNAL, INSERT_BACKPACK, INSERT_PLAYER,
        
        UPDATE_CONVERSATION,
        UPDATE_LANGUAGE
    }

}
