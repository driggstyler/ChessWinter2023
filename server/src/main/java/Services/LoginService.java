package Services;

import DAO.AuthtokenDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import Models.Authtoken;
import Models.User;
import Requests.LoginRequest;
import Results.LoginResult;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * A service to handle the logic for the login operation.
 */
public class LoginService {
    /**
     * Log a user into the server.
     * @param loginRequest Contains the information the user needs to log in.
     * @return A LogInResult object containing the results of the login operation.
     */
    public LoginResult Execute(LoginRequest loginRequest){
        LoginResult loginResult = new LoginResult();
        //Database db = new Database();
        try (Connection conn = DatabaseManager.getConnection()) {
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(conn);
            UserDAO userDAO = new UserDAO(conn);
            User user = userDAO.Find(loginRequest.getUsername());
            if (user != null) {
                if (user.getPassword().equals(loginRequest.getPassword())) {
                    String authtoken = UUID.randomUUID().toString();
                    authtokenDAO.Insert(new Authtoken(authtoken, loginRequest.getUsername()));
                    loginResult.setUsername(loginRequest.getUsername());
                    loginResult.setAuthtoken(authtoken);
                    loginResult.setSuccess(true);
                    loginResult.setMessage("Logged in successfully.");
                }
                else {
                    loginResult.setSuccess(false);
                    loginResult.setMessage("Error: Incorrect password.");
                }
                //db.closeConnection(db.getConnection());
            }
            else {
                //db.closeConnection(db.getConnection());
                loginResult.setSuccess(false);
                loginResult.setMessage("Error: User not found in the database.");
            }
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            loginResult.setSuccess(false);
            loginResult.setMessage("Error in login.");
        }
        return loginResult;
    }
}
