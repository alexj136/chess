package net.alexjeffery.chess.game;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Board {

    public static final int NUM_RANKS = 8; // rows, horizontal
    public static final int NUM_FILES = 8; // columns, vertical

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
        this.board = new byte[NUM_RANKS * NUM_FILES];
        int fenCharPtr = 0;

        // Process the layout
        for(int boardPtr = 0; fenCharPtr < fenString.length() && fenString.charAt(fenCharPtr) != ' '; fenCharPtr++) {
            char currentFenChar = fenString.charAt(fenCharPtr);
            if (Character.isAlphabetic(currentFenChar)) {
                try {
                    board[boardPtr++] = Piece.fromFenChar(currentFenChar);
                } catch (IllegalArgumentException e) {
                    fenParseError(currentFenChar, fenCharPtr);
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
                fenParseError(currentFenChar, fenCharPtr);
            }
        }

        // skip over the space
        if (fenCharPtr >= fenString.length()) {
            fenParseErrorEof();
        }
        if (fenString.charAt(fenCharPtr) != ' ') {
            fenParseError(fenString.charAt(fenCharPtr), fenCharPtr);
        }
        fenCharPtr++;

        // process the active player
        if(fenCharPtr < fenString.length()) {
            char currentFenChar = fenString.charAt(fenCharPtr);
            switch (currentFenChar) {
                case 'w':
                case 'W':
                    this.whiteToMove = true;
                    break;
                case 'b':
                case 'B':
                    this.whiteToMove = false;
                    break;
                default:
                    fenParseError(currentFenChar, fenCharPtr);
            }
            fenCharPtr++;
        }
        else {
            fenParseErrorEof();
        }

        // skip over the space
        if (fenCharPtr >= fenString.length()) {
            fenParseErrorEof();
        }
        if (fenString.charAt(fenCharPtr) != ' ') {
            fenParseError(fenString.charAt(fenCharPtr), fenCharPtr);
        }
        fenCharPtr++;

        // process the castling information
        this.castling = new boolean[] { false, false, false, false };
        for(; fenCharPtr < fenString.length() && fenString.charAt(fenCharPtr) != ' '; fenCharPtr++) {
            char currentFenChar = fenString.charAt(fenCharPtr);
            switch (currentFenChar) {
                case 'K':
                    this.castling[WHITE_KINGSIDE] = true;
                    break;
                case 'Q':
                    this.castling[WHITE_QUEENSIDE] = true;
                    break;
                case 'k':
                    this.castling[BLACK_KINGSIDE] = true;
                    break;
                case 'q':
                    this.castling[BLACK_QUEENSIDE] = true;
                    break;
                case '-':
                    break;
                default:
                    fenParseError(currentFenChar, fenCharPtr);
            }
        }

        // skip over the space
        if (fenCharPtr >= fenString.length()) {
            fenParseErrorEof();
        }
        if (fenString.charAt(fenCharPtr) != ' ') {
            fenParseError(fenString.charAt(fenCharPtr), fenCharPtr);
        }
        fenCharPtr++;

        // process the en passant status
        if(fenCharPtr < fenString.length()) {
            char rankChar = fenString.charAt(fenCharPtr);
            char rankCharLower = Character.toLowerCase(rankChar);
            if (rankCharLower >= 'a' && rankCharLower <= 'h') {
                fenCharPtr++;
                if(fenCharPtr < fenString.length()) {
                    char fileChar = fenString.charAt(fenCharPtr);
                    if (fileChar >= '1' && fileChar <= '8') {
                        this.enPassant = (rankCharLower - 'a') * NUM_RANKS + (fileChar - '1');
                        fenCharPtr++;
                    }
                    else {
                        fenParseError(fileChar, fenCharPtr);
                    }
                }
                else {
                    fenParseErrorEof();
                }
            }
            else if (rankChar == '-') {
                this.enPassant = null;
                fenCharPtr++;
            }
            else {
                fenParseError(rankChar, fenCharPtr);
            }
        }
        else {
            fenParseErrorEof();
        }

        // skip over the space
        if (fenCharPtr >= fenString.length()) {
            fenParseErrorEof();
        }
        if (fenString.charAt(fenCharPtr) != ' ') {
            fenParseError(fenString.charAt(fenCharPtr), fenCharPtr);
        }
        fenCharPtr++;

        // process the half-move clock
        {
            int nextSpaceIndex = fenString.indexOf(' ', fenCharPtr);
            if (nextSpaceIndex > 0) {
                String numString = fenString.substring(fenCharPtr, nextSpaceIndex);
                try {
                    this.halfMoveClock = Integer.parseInt(numString);
                } catch (NumberFormatException e) {
                    fenParseError("Error processing FEN string: " + e.getMessage());
                }
                fenCharPtr = nextSpaceIndex;
            }
            else {
                fenParseErrorEof();
            }
        }

        // skip over the space
        if (fenCharPtr >= fenString.length()) {
            fenParseErrorEof();
        }
        if (fenString.charAt(fenCharPtr) != ' ') {
            fenParseError(fenString.charAt(fenCharPtr), fenCharPtr);
        }
        fenCharPtr++;

        // process the full move number
        {
            String numString = fenString.substring(fenCharPtr);
            try {
                this.fullMoveNumber = Integer.parseInt(numString);
            } catch (NumberFormatException e) {
                fenParseError("Error processing FEN string: " + e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int rank = 0, boardPtr = 0; rank < NUM_RANKS; rank++) {
            for (int file = 0; file < NUM_FILES; file++, boardPtr++) {
                byte piece = this.board[boardPtr];
                builder.append(piece == Piece.NONE ? ' ' : Piece.fenChar(piece));
            }
            builder.append('\n');
        }
        builder.append((this.whiteToMove ? "White" : "Black") + " to move\n");
        if (this.castling[WHITE_KINGSIDE])
            builder.append("White can castle kingside\n");
        if (this.castling[WHITE_QUEENSIDE])
            builder.append("White can castle queenside\n");
        if (this.castling[BLACK_KINGSIDE])
            builder.append("Black can castle kingside\n");
        if (this.castling[BLACK_QUEENSIDE])
            builder.append("Black can castle queenside\n");
        if (this.enPassant != null)
            builder.append("En passant: " + this.enPassant + "\n");
        builder.append("Half-move clock: " + this.halfMoveClock + "\n");
        builder.append("Full-move number: " + this.fullMoveNumber + "\n");
        return builder.toString();
    }

    public static void fenParseError(char c, int pos) {
        throw new RuntimeException("Error processing FEN string: unrecognised character '"
                + c + "' at position " + (pos + 1) + ".");
    }

    public static void fenParseError(String str) {
        throw new RuntimeException(str);
    }

    public static void fenParseErrorEof() {
        throw new RuntimeException("Error processing FEN string: string incomplete.");
    }
}