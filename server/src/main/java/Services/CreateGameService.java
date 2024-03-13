package Services;

import DAO.AuthtokenDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import Models.Game;
import Requests.CreateGameRequest;
import Results.CreateGameResult;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
//import mainChess.Game;

/**
 * A service to handle the logic for the create game operation.
 */
public class CreateGameService {
    /**
     * Creates a new game in the database.
     * @param createGameRequest Containing the information needed to create the new game.
     * @param authtoken The authtoken of the signed-in user.
     * @return A CreateGameResult the contains the results of the createGame operation.
     */
    public CreateGameResult Execute(CreateGameRequest createGameRequest, String authtoken) {
        CreateGameResult createGameResult = new CreateGameResult();
        //DatabaseManager db = new DatabaseManager();
        try (Connection conn = DatabaseManager.getConnection()){
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(conn);
            GameDAO gameDAO = new GameDAO(conn);
            if (authtokenDAO.Find(authtoken) == null) {
                createGameResult.setSuccess(false);
                createGameResult.setMessage("Error: Unauthorized");
                return createGameResult;
            }
            if (createGameRequest.getGameName() == null) {
                createGameResult.setSuccess(false);
                createGameResult.setMessage("Error: Bad request.");
                return createGameResult;
            }

            String gameID;
            if (gameDAO.FindAll() != null) {
                gameID = Integer.toString(gameDAO.FindAll().size() + 1);
            }
            else {
                gameID = "1";
            }
            gameDAO.Insert(gameID, new Game(), createGameRequest.getGameName());
            //db.closeConnection(db.getConnection());
            createGameResult.setGameID(gameID);
            createGameResult.setSuccess(true);
            createGameResult.setMessage("Successfully created a new game.");
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            createGameResult.setGameID(null);
            createGameResult.setSuccess(false);
            createGameResult.setMessage("Error in creating a new game.");
        }
        return createGameResult;
    }
}
