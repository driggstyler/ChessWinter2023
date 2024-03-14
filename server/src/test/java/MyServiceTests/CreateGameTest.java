package MyServiceTests;

//import DAO.AuthtokenDAO;
import Models.Authtoken;
import Requests.CreateGameRequest;
import Results.CreateGameResult;
import Services.ClearService;
import Services.CreateGameService;
import dataAccess.DAO.AuthtokenDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

public class CreateGameTest {
    @BeforeEach
    public void setup(){}
    @Test
    @DisplayName("Create Game Success")
    public void createGameSuccess(){
        try (Connection conn = DatabaseManager.getConnection()){
            Authtoken authtoken = new Authtoken("abcdefg", "Zanmorn");
            AuthtokenDAO authtokenDAO = new AuthtokenDAO(conn);
            authtokenDAO.Insert(authtoken);
            CreateGameRequest createGameRequest = new CreateGameRequest("testGame1");
            CreateGameService createGameService = new CreateGameService();
            CreateGameResult createGameResult = createGameService.Execute(createGameRequest, authtoken.getAuthtoken());
            Assertions.assertEquals("Successfully created a new game.", createGameResult.getMessage());
        }
        catch (DataAccessException | SQLException e) {
            System.out.println("CreateGameTest threw exception.");
        }
    }
    @Test
    @DisplayName("Create Game Failure")
    public void createGameFail() {
        CreateGameRequest createGameRequest = new CreateGameRequest("testGame1");
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.Execute(createGameRequest, "zyx");
        Assertions.assertEquals("Error: Unauthorized", createGameResult.getMessage());
    }
    @AfterEach
    public void tearDown() {
        ClearService clearService = new ClearService();
        clearService.Execute();
    }
}
