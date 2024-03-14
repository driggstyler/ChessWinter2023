package Services;

import dataAccess.DAO.AuthtokenDAO;
import dataAccess.DAO.GameDAO;
import Requests.JoinGameRequest;
import Results.JoinGameResult;
import dataAccess.DataAccessException;
//import dataAccess.Database;
import dataAccess.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A service to handle the logic for the join game operation.
 */
public class JoinGameService {
    /**
     * Makes a user join a game.
     * @param joinGameRequest The information required to join a game.
     * @param authtoken The authoke of the signed-in user.
     * @return A JoinGameResult object containing the results of the joinGame operation.
     */
    public JoinGameResult Execute(JoinGameRequest joinGameRequest, String authtoken){
        JoinGameResult joinGameResult = new JoinGameResult();
        //DatabaseManager db = new DatabaseManager();
        try (Connection conn = DatabaseManager.getConnection()) {
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(conn);
            GameDAO gameDAO = new GameDAO(conn);
            if (authtokenDAO.Find(authtoken) == null) {
                joinGameResult.setSuccess(false);
                joinGameResult.setMessage("Error: Unauthorized.");
                //db.closeConnection(db.getConnection());
                return joinGameResult;
            }
            if (gameDAO.Find(joinGameRequest.getGameID()) == null) {
                joinGameResult.setSuccess(false);
                joinGameResult.setMessage("Error: Bad request.");
                //db.closeConnection(db.getConnection());
                return joinGameResult;
            }
            if (joinGameRequest.getPlayerColor() != null) {
                boolean claimedSpot = gameDAO.claimSpot(joinGameRequest.getGameID(), joinGameRequest.getPlayerColor(), authtokenDAO.Find(authtoken).getUsername());
                if (!claimedSpot) {
                    joinGameResult.setSuccess(false);
                    joinGameResult.setMessage("Error: Already taken.");
                    //db.closeConnection(db.getConnection());
                    return joinGameResult;
                }
            }
            //db.closeConnection(db.getConnection());
            joinGameResult.setSuccess(true);
            joinGameResult.setMessage("Joined game successfully.");
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            joinGameResult.setMessage("Error in joining game.");
        }
        return joinGameResult;
    }
}
