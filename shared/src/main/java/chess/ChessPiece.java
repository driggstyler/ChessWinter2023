package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> possibleMoves = new HashSet<>();
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.ROOK) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int l = 1; l <= 8; l++) {
                        if (i == j || (i == 1 && j == -1) || (i == -1 && j == 1)) {
                            break;
                        }
                        ChessPosition currPosition = new ChessPosition(myPosition.getRow() + (i*l), myPosition.getColumn() + (j*l));
                        if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                            break;
                        }
                        if (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                        if (board.getPiece(currPosition) != null) {
                            break;
                        }
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.BISHOP) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int l = 1; l <= 8; l++) {
                        if (i == 0 || j == 0) {
                            break;
                        }
                        ChessPosition currPosition = new ChessPosition(myPosition.getRow() + (i*l), myPosition.getColumn() + (j*l));
                        if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                            break;
                        }
                        if (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                        if (board.getPiece(currPosition) != null) {
                            break;
                        }
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.QUEEN) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int l = 1; l <= 8; l++) {
                        ChessPosition currPosition = new ChessPosition(myPosition.getRow() + (i*l), myPosition.getColumn() + (j*l));
                        if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                            break;
                        }
                        if (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                        if (board.getPiece(currPosition) != null) {
                            break;
                        }
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.KING) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int l = 1; l <= 8; l++) {
                        if (l > 1) {
                            break;
                        }
                        ChessPosition currPosition = new ChessPosition(myPosition.getRow() + (i * l), myPosition.getColumn() + (j * l));
                        if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                            break;
                        }
                        if (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                        if (board.getPiece(currPosition) != null) {
                            break;
                        }
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.KNIGHT) {
            for (int i = 2; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (i != j) {
                        ChessPosition currPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
                        if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                                && (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor)) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                        currPosition = new ChessPosition(currPosition.getRow() - (i * 2), currPosition.getColumn());
                        if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                                && (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor)) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                        currPosition = new ChessPosition(currPosition.getRow(), currPosition.getColumn() - (j * 2));
                        if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                                && (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor)) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                        currPosition = new ChessPosition(currPosition.getRow() + (i * 2), currPosition.getColumn());
                        if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
                                && (board.getPiece(currPosition) == null || board.getPiece(currPosition).getTeamColor() != pieceColor)) {
                            ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                            possibleMoves.add(chessMove);
                        }
                    }
                }
            }
        }
        if (piece.getPieceType() == PieceType.PAWN) {
            ChessPosition currPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
            ChessPosition blocker =  new ChessPosition(myPosition.getRow(), myPosition.getColumn());
            boolean hasMoved = false;
            if ((myPosition.getRow() != 2 && pieceColor == ChessGame.TeamColor.WHITE) || (myPosition.getRow() != 7 && pieceColor == ChessGame.TeamColor.BLACK)) {
                hasMoved = true;
            }
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                currPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                blocker = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            }
            if (pieceColor == ChessGame.TeamColor.BLACK) {
                currPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                blocker = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            }
            if ((currPosition.getRow() > 0 && currPosition.getRow() < 9 && currPosition.getColumn() > 0 && currPosition.getColumn() < 9)
            && (!hasMoved && board.getPiece(currPosition) == null && board.getPiece(blocker) == null)) {
                ChessMove chessMove = new ChessMove(myPosition, currPosition, null);
                possibleMoves.add(chessMove);
            }
            for (int i = -1; i <= 1; i++) {
                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    currPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + i);
                }
                if (pieceColor == ChessGame.TeamColor.BLACK) {
                    currPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + i);
                }
                if (currPosition.getRow() < 1 || currPosition.getRow() > 8 || currPosition.getColumn() < 1 || currPosition.getColumn() > 8) {
                    continue;
                }
                if ((i == 0 && board.getPiece(currPosition) == null)
                        || (i != 0 && board.getPiece(currPosition) != null && board.getPiece(currPosition).getTeamColor() != pieceColor)) {
                    if ((pieceColor == ChessGame.TeamColor.WHITE && currPosition.getRow() == 8)
                        || pieceColor == ChessGame.TeamColor.BLACK && currPosition.getRow() == 1) {
                        possibleMoves.add(new ChessMove(myPosition, currPosition, PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(myPosition, currPosition, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, currPosition, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, currPosition, PieceType.ROOK));
                    }
                    else {
                        possibleMoves.add(new ChessMove(myPosition, currPosition, null));
                    }
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece chessPiece = (ChessPiece) o;
        return pieceColor == chessPiece.getTeamColor() && type == chessPiece.getPieceType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
