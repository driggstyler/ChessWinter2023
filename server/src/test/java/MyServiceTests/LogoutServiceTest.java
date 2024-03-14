package MyServiceTests;

import Models.Authtoken;
import Results.LogoutResult;
import Services.ClearService;
import Services.LogoutService;
import dataAccess.DAO.AuthtokenDAO;
import dataAccess.DAO.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class LogoutServiceTest {
    @BeforeEach
    public void setup() {
        try (Connection conn = DatabaseManager.getConnection()) {
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(conn);
            GameDAO gameDAO = new GameDAO(conn);
            authtokenDAO.clear();
            gameDAO.clear();
        }
        catch (DataAccessException | SQLException e) {
            System.out.println("JoinGameTest setup threw an exception.");
        }
    }
    @Test
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        try (Connection conn = DatabaseManager.getConnection()) {
            Authtoken authtoken = new Authtoken("abcdefg", "testUser1");
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(conn);
            authtokenDAO.Insert(authtoken);
            LogoutService logoutService = new LogoutService();
            LogoutResult logoutResult = logoutService.Execute("abcdefg");
            Assertions.assertEquals("Logged out successfully.", logoutResult.getMessage());
        }
        catch (DataAccessException | SQLException e) {
            System.out.println("Logout Service Test threw an exception.");
        }
    }
    @Test
    @DisplayName("Logout Failure")
    public void logoutFail() {
        LogoutService logoutService = new LogoutService();
        LogoutResult logoutResult = logoutService.Execute("abcdefg");
        Assertions.assertEquals("Error: Unauthorized", logoutResult.getMessage());
    }
    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.Execute();
    }
}
