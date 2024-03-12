package Services;

import DAO.AuthtokenDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import Models.User;
import Requests.JoinGameRequest;
import Results.JoinGameResult;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.Database;

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
        Database db = new Database();
        try {
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(db.getConnection());
            GameDAO gameDAO = new GameDAO(db.getConnection());
            if (authtokenDAO.Find(authtoken) == null) {
                joinGameResult.setSuccess(false);
                joinGameResult.setMessage("Error: Unauthorized.");
                db.closeConnection(db.getConnection());
                return joinGameResult;
            }
            if (gameDAO.Find(String.valueOf(joinGameRequest.getGameID())) == null) {
                joinGameResult.setSuccess(false);
                joinGameResult.setMessage("Error: Bad request.");
                db.closeConnection(db.getConnection());
                return joinGameResult;
            }
            if (joinGameRequest.getPlayerColor() != null) {
                boolean claimedSpot = gameDAO.claimSpot(String.valueOf(joinGameRequest.getGameID()), joinGameRequest.getPlayerColor(), authtokenDAO.Find(authtoken).getUsername());
                if (!claimedSpot) {
                    joinGameResult.setSuccess(false);
                    joinGameResult.setMessage("Error: Already taken.");
                    db.closeConnection(db.getConnection());
                    return joinGameResult;
                }
            }
            db.closeConnection(db.getConnection());
            joinGameResult.setSuccess(true);
            joinGameResult.setMessage("Joined game successfully.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            joinGameResult.setMessage("Error in joining game.");
        }
        return joinGameResult;
    }
}
