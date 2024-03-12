package Results;

import chess.ChessGame;

import java.util.ArrayList;

public class ListGamesResult {
    private ArrayList<ChessGame> games;
    private String message;
    private boolean success;

    public ArrayList<ChessGame> getGames() {
        return games;
    }

    public void setGames(ArrayList<ChessGame> games) {
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
