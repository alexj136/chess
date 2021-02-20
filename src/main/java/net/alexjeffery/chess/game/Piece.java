package net.alexjeffery.chess.game;

/**
 * We represent a piece with a byte. This class contains static methods for manipulating them.
 *
 * The type of piece is represented by the decimal unit (1 through 7), and the colour is represented by the ten
 * (10 and 20). Whether or not the piece has moved is represented by the hundred (100 for moved, 0 for unmoved).
 */
public class Piece {
    public static final byte NONE = 0;

    public static final byte PAWN = 1;
    public static final byte ROOK = 2;
    public static final byte KNIGHT = 3;
    public static final byte BISHOP = 4;
    public static final byte QUEEN = 5;
    public static final byte KING = 7;

    public static final byte WHITE = 10;
    public static final byte BLACK = 20;

    public static boolean isType(byte piece, byte type) {
        return getType(piece) == type;
    }

    public static byte getType(byte piece) {
        return (byte) (piece % WHITE);
    }

    public static boolean isWhite(byte piece) {
        return piece < BLACK;
    }

    public static boolean isBlack(byte piece) {
        return piece >= BLACK;
    }

    public static char fenChar(byte piece) {
        char whitePiece = fenCharForType(getType(piece));
        return isBlack(piece) ? Character.toLowerCase(whitePiece) : whitePiece;
    }

    private static char fenCharForType(byte type) {
        if (type == PAWN) return 'P';
        if (type == ROOK) return 'R';
        if (type == KNIGHT) return 'N';
        if (type == BISHOP) return 'B';
        if (type == QUEEN) return 'Q';
        if (type == KING) return 'K';
        throw new IllegalArgumentException("" + type);
    }

    public static byte fromFenChar(char fenChar) {
        boolean white = Character.isUpperCase(fenChar);
        final byte type;
        switch (("" + fenChar).toUpperCase().charAt(0)) {
            case 'P': type = PAWN; break;
            case 'R': type = ROOK; break;
            case 'N': type = KNIGHT; break;
            case 'B': type = BISHOP; break;
            case 'Q': type = QUEEN; break;
            case 'K': type = KING; break;
            default: return NONE;
        }
        return (byte) ((white ? WHITE : BLACK) + type);
    }
}
