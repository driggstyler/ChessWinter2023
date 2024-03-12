package Services;

import DAO.AuthtokenDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import Models.Authtoken;
import Models.User;
import Requests.RegisterRequest;
import Results.RegisterResult;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.util.UUID;

/**
 * A service to handle the logic for the register operation.
 */
public class RegisterService {
    /**
     * Register a new user into the database.
     * @param registerRequest Contains the information needed to register the new user.
     * @return A RegisterResult object containing the results of the register operation.
     */
    public RegisterResult Execute(RegisterRequest registerRequest){
        RegisterResult registerResult = new RegisterResult();
        Database db = new Database();
        try {
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(db.getConnection());
            UserDAO userDAO = new UserDAO(db.getConnection());
            if (registerRequest.getUsername() == null ||
                    registerRequest.getPassword() == null) {
                registerResult.setSuccess(false);
                registerResult.setMessage("Error: Missing information to register.");
                db.closeConnection(db.getConnection());
                return registerResult;
            }
            if (userDAO.Find(registerRequest.getUsername()) != null) {
                registerResult.setSuccess(false);
                registerResult.setMessage("Error: Username already taken.");
                db.closeConnection(db.getConnection());
                return registerResult;
            }
            String authtoken = UUID.randomUUID().toString();
            authtokenDAO.Insert(new Authtoken(authtoken, registerRequest.getUsername()));
            userDAO.Insert(new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail()));
            registerResult.setUsername(registerRequest.getUsername());
            registerResult.setAuthtoken(authtoken);
            registerResult.setSuccess(true);
            registerResult.setMessage("Registered successfully.");
            db.closeConnection(db.getConnection());
        } catch (DataAccessException e) {
            e.printStackTrace();
            registerResult.setSuccess(false);
            registerResult.setMessage("Error in registering.");
        }
        return registerResult;
    }
}
