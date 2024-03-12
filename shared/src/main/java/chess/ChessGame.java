package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamColor = TeamColor.WHITE;
    private ChessBoard board;
    private String whiteUsername = null;
    private String blackUsername = null;
    private String gameName = null;
    private int gameID;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> possibleMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> result = new HashSet<>();
        //Find active player's king
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currPosition).getTeamColor() == board.getPiece(startPosition).getTeamColor()) {
                    kingPosition = currPosition;
                }
            }
        }
        for (ChessMove element : possibleMoves) {
            ChessPiece capturedPiece = board.getPiece(element.getEndPosition());
            board.addPiece(element.getEndPosition(), board.getPiece(element.getStartPosition()));
            board.remove(element.getStartPosition());
            if (isInCheck(board.getPiece(element.getEndPosition()).getTeamColor())) {
                board.addPiece(element.getStartPosition(), board.getPiece(element.getEndPosition()));
                board.remove(element.getEndPosition());
                if (capturedPiece != null) {
                    board.addPiece(element.getEndPosition(), capturedPiece);
                }
            } else {
                board.addPiece(element.getStartPosition(), board.getPiece(element.getEndPosition()));
                board.remove(element.getEndPosition());
                if (capturedPiece != null) {
                    board.addPiece(element.getEndPosition(), capturedPiece);
                }
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (teamColor != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move)) {
            if (move.getPromotionPiece() != null) {
                if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
                    board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), ChessPiece.PieceType.QUEEN));
                } else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
                    board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), ChessPiece.PieceType.BISHOP));

                } else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT) {
                    board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), ChessPiece.PieceType.KNIGHT));

                } else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
                    board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), ChessPiece.PieceType.ROOK));
                }
            } else {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            }
            board.remove(move.getStartPosition());
        } else {
            throw new InvalidMoveException();
        }
        if (teamColor == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //Find active player's king
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i,j);
                if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currPosition).getTeamColor() == teamColor) {
                    kingPosition = currPosition;
                }
            }
        }
        //If for some reason there is no king to check
        if (kingPosition == null) {
            return false;
        }
        //Check for enemy knights
        for (int i = 2; i >= 1; i--) {
            for (int j = 1; j <= 2; j++) {
                if (i != j) {
                    ChessPosition currPosition = new ChessPosition(kingPosition.getRow() + i, kingPosition.getColumn() + j);
                    if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                            && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KNIGHT && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor())) {
                        return true;
                    }
                    currPosition = new ChessPosition(currPosition.getRow() - (i * 2), currPosition.getColumn());
                    if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                            && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KNIGHT && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor())) {
                        return true;
                    }
                    currPosition = new ChessPosition(currPosition.getRow(), currPosition.getColumn() - (j * 2));
                    if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                            && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KNIGHT && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor())) {
                        return true;
                    }
                    currPosition = new ChessPosition(currPosition.getRow() + (i * 2), currPosition.getColumn());
                    if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                            && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KNIGHT && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor())) {
                        return true;
                    }
                }
            }
        }
        //Check for unblocked enemy queen, bishops, and rooks (also checks for enemy king to prevent check by king)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int l = 1; l <= 8; l++) {
                    ChessPosition currPosition = new ChessPosition(kingPosition.getRow() + (i * l), kingPosition.getColumn() + (j * l));
                    //NEW Check if currPosition is out of bounds
                    if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                        break;
                    }
                    // If the piece is the enemy king (cpas below)
                    if (l == 1 && board.getPiece(currPosition) != null && (board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor())) {
                        return true;
                    }
                    // If potential check is blocked (enemy pawn and king are checked properly after this)
                    if (board.getPiece(currPosition) != null && ((board.getPiece(currPosition).getTeamColor() == board.getPiece(kingPosition).getTeamColor()) || board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.PAWN || board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KNIGHT || board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING)) {
                        break;
                    }

                    // If the piece is an enemy queen
                    if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.QUEEN && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor()) {
                        return true;
                    }
                    // Check if there's an enemy bishop or a rook on a diagonal
                    if (i != 0 && j != 0) {
                        if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.BISHOP && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor()) {
                            return true;
                        }
                        if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.ROOK && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor()) {
                            break;
                        }
                    }
                    // Check if there's an enemy rook or a bishop on a straight
                    else {
                        if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.ROOK && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor()) {
                            return true;
                        }
                        if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.BISHOP && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor()) {
                            break;
                        }
                    }
                }
            }
        }
        //Check for pawns
        //try: iterate through enemy pieces and their possible moves, if their end position equals king position, king is in check.
        if (board.getPiece(kingPosition).getTeamColor() == TeamColor.WHITE) {
            ChessPosition currPosition = new ChessPosition(kingPosition.getRow() + 1, kingPosition.getColumn() - 1);
            if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                    && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(currPosition).getTeamColor() == TeamColor.BLACK)) {
                return true;
            }
            currPosition = new ChessPosition(kingPosition.getRow() + 1, kingPosition.getColumn() + 1);
            if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                    && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(currPosition).getTeamColor() == TeamColor.BLACK)) {
                return true;
            }
        }
        if (board.getPiece(kingPosition).getTeamColor() == TeamColor.BLACK) {
            ChessPosition currPosition = new ChessPosition(kingPosition.getRow() - 1, kingPosition.getColumn() - 1);
            if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                    && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(currPosition).getTeamColor() == TeamColor.WHITE)) {
                return true;
            }
            currPosition = new ChessPosition(kingPosition.getRow() - 1, kingPosition.getColumn() + 1);
            if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                    && (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(currPosition).getTeamColor() == TeamColor.WHITE)) {
                return true;
            }
        }
        // Every possibility was checked and no checks were found
        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //Find active player's king
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currPosition).getTeamColor() == teamColor) {
                    kingPosition = currPosition;
                }
            }
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                ChessPosition currPosition = new ChessPosition(kingPosition.getRow() + i, kingPosition.getColumn() + j);
                if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                    break;
                }
                if (board.getPiece(currPosition) == null || (board.getPiece(currPosition) != null && ((board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor()) || board.getPiece(currPosition) == board.getPiece(kingPosition)))) {
                    if (board.getPiece(currPosition) == board.getPiece(kingPosition)) {
                        if (isInCheck(teamColor)) {
                            continue;
                        }
                    }
                    ChessPiece capturedPiece = null;
                    if (board.getPiece(currPosition) != null) {
                        capturedPiece = board.getPiece(currPosition);
                    }
                    board.addPiece(currPosition, board.getPiece(kingPosition));
                    board.remove(kingPosition);
                    if (!isInCheck(teamColor)) {
                        board.addPiece(kingPosition, board.getPiece(currPosition));
                        board.remove(currPosition);
                        if (capturedPiece != null) {
                            board.addPiece(currPosition, capturedPiece);
                        }
                        return false;
                    } else {
                        board.addPiece(kingPosition, board.getPiece(currPosition));
                        board.remove(currPosition);
                        if (capturedPiece != null) {
                            board.addPiece(currPosition, capturedPiece);
                        }
                    }

                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //Find active player's king
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                if (board.getPiece(currPosition) != null && board.getPiece(currPosition).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(currPosition).getTeamColor() == teamColor) {
                    kingPosition = currPosition;
                }
            }
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                ChessPosition currPosition = new ChessPosition(kingPosition.getRow() + i, kingPosition.getColumn() + j);
                if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                    continue;
                }
                if (board.getPiece(currPosition) == null || (board.getPiece(currPosition) != null && board.getPiece(currPosition).getTeamColor() != board.getPiece(kingPosition).getTeamColor())) {
                    ChessPiece capturedPiece = null;
                    if (board.getPiece(currPosition) != null) {
                        capturedPiece = board.getPiece(currPosition);
                    }
                    board.addPiece(currPosition, board.getPiece(kingPosition));
                    board.remove(kingPosition);
                    if (!isInCheck(teamColor)) {
                        board.addPiece(kingPosition, board.getPiece(currPosition));
                        board.remove(currPosition);
                        if (capturedPiece != null) {
                            board.addPiece(currPosition, capturedPiece);
                        }
                        return false;
                    } else {
                        board.addPiece(kingPosition, board.getPiece(currPosition));
                        board.remove(currPosition);
                        if (capturedPiece != null) {
                            board.addPiece(currPosition, capturedPiece);
                        }
                    }

                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
    //Added other getters and setters

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
