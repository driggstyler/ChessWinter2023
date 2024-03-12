package Services;

import DAO.AuthtokenDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import Results.ClearResult;
import dataAccess.DataAccessException;
import dataAccess.Database;

/**
 * A service to handle the logic for the clear operation.
 */
public class ClearService {
    /**
     * Clears everything in the database (Authtokens, Games, and Users).
     * @return A ClearResponse object containing the results of the clear operation.
     */
    public ClearResult Execute() {
        ClearResult clearResult = new ClearResult();
        Database db = new Database();
        try {
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(db.getConnection());
            GameDAO gameDAO = new GameDAO(db.getConnection());
            UserDAO userDAO = new UserDAO(db.getConnection());
            authtokenDAO.clear();
            gameDAO.clear();
            userDAO.clear();
            db.closeConnection(db.getConnection());
            clearResult.setSuccess(true);
            clearResult.setMessage("Clear succeeded.");
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            clearResult.setSuccess(false);
            clearResult.setMessage("Error occured while trying to clear the database.");
        }
        return clearResult;
    }
}
