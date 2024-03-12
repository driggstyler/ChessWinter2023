package DAO;

import Models.Game;
import chess.ChessGame;
import com.google.gson.*;
//import mainChess.Game;

import java.lang.reflect.Type;

public class GameAdapter implements JsonDeserializer<ChessGame> {
    @Override
    public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonDeserializationContext.deserialize(jsonElement, Game.class);
    }
}
