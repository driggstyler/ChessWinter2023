package Services;

import DAO.AuthtokenDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import Models.Game;
import Results.ListGamesResult;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A service to handle the logic for the list games operation.
 */
public class ListGamesService {
    /**
     * Lists all the games in the database.
     * @param authtoken The authtoken of the signed-in user.
     * @return A ListGamesResult object containing the results of the listGames operation.
     */
    public ListGamesResult Execute(String authtoken){
        ListGamesResult listGamesResult = new ListGamesResult();
        //Database db = new Database();
        try (Connection conn = DatabaseManager.getConnection()){
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(conn);
            GameDAO gameDAO = new GameDAO(conn);
            if (authtokenDAO.Find(authtoken) == null) {
                listGamesResult.setSuccess(false);
                listGamesResult.setMessage("Error: Unauthorized");
                return listGamesResult;
            }
            ArrayList<Game> games = gameDAO.FindAll();
            //db.closeConnection(db.getConnection());
            listGamesResult.setGames(games);
            listGamesResult.setMessage("Listed games successfully.");
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            listGamesResult.setMessage("Error in listing games.");
        }
        return listGamesResult;
    }
}
