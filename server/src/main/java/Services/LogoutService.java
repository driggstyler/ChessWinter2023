package Services;

import DAO.AuthtokenDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import Results.LogoutResult;
import dataAccess.DataAccessException;
import dataAccess.Database;

/**
 * A service to handle the logic for the logout operation.
 */
public class LogoutService {
    /**
     * Logs the user out of the server.
     * @param authtoken The authtoken of the signed-in user.
     * @return A LogoutResult object containing the results of the logout operation.
     */
    public LogoutResult Execute(String authtoken){
        LogoutResult logoutResult = new LogoutResult();
        Database db = new Database();
        try {
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(db.getConnection());
            if (authtokenDAO.Find(authtoken) == null) {
                logoutResult.setSuccess(false);
                logoutResult.setMessage("Error: Unauthorized");
                return logoutResult;
            }
            authtokenDAO.Remove(authtoken);
            //FIXME make sure that auth exists
            db.closeConnection(db.getConnection());
            logoutResult.setSuccess(true);
            logoutResult.setMessage("Logged out successfully.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            logoutResult.setSuccess(false);
            logoutResult.setMessage("Error in logging out.");
        }
        return logoutResult;
    }
}
