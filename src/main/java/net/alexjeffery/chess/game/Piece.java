package net.alexjeffery.chess.game;

public enum Piece {
    NONE,
    PAWN_W,
    PAWN_B,
    ROOK_W,
    ROOK_B,
    KNIGHT_W,
    KNIGHT_B,
    BISHOP_W,
    BISHOP_B,
    QUEEN_W,
    QUEEN_B,
    KING_W,
    KING_B;

    public boolean isWhite() {
        return this.toString().endsWith("W");
    }

    public boolean isBlack() {
        return this.toString().endsWith("B");
    }

    public boolean isPiece() {
        return this != NONE;
    }
}
