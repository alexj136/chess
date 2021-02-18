package net.alexjeffery.chess.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Board {

    public static final int BOARD_RANKS = 8; // rows, horizontal
    public static final int BOARD_FILES = 8; // columns, vertical

    public static final String START_FEN_STRING = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    // Named indices into castling arrays
    public static final int WHITE_KINGSIDE = 0;
    public static final int WHITE_QUEENSIDE = 1;
    public static final int BLACK_KINGSIDE = 2;
    public static final int BLACK_QUEENSIDE = 3;

    @NotNull
    private byte[] board;

    private boolean whiteToMove;

    @NotNull
    private boolean[] castling;

    @Nullable
    private Integer enPassant;

    private int halfMoveClock;

    private int fullMoveNumber;

    public Board() {
        this(START_FEN_STRING);
    }

    public Board(String fenString) {
        this.board = new byte[BOARD_RANKS * BOARD_FILES];

        int fenCharPtr = 0;
        // Process the layout
        for(int boardPtr = 0; fenCharPtr < fenString.length() && fenString.charAt(fenCharPtr) != ' '; fenCharPtr++) {
            char currentFenChar = fenString.charAt(fenCharPtr);
            if (Character.isAlphabetic(currentFenChar)) {
                try {
                    board[boardPtr++] = Piece.fromFenChar(currentFenChar);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Error processing FEN string: unrecognised character '"
                            + currentFenChar + "' in piece placement section.");
                }
            }
            else if (Character.isDigit(currentFenChar) && !"09".contains("" + currentFenChar)) {
                for (int i = 0; i < Integer.parseInt("" + currentFenChar); i++)
                    board[boardPtr++] = Piece.NONE;
            }
            else if (currentFenChar == '/') {
                // pass
            }
            else {
                throw new RuntimeException("Error processing FEN string: unrecognised character '"
                        + currentFenChar + "' in piece placement section.");
            }
        }

        // TODO: skip over the space
        // TODO: process the active player
        // TODO: skip over the space
        // TODO: process the castling information
        // TODO: skip over the space
        // TODO: process the en passant status
        // TODO: skip over the space
        // TODO: process the half-move clock
        // TODO: skip over the space
        // TODO: process the full move number
    }
}