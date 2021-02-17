package net.alexjeffery.chess.game;

public class Board {

    private Piece[][] board;

    public static final int BOARD_RANKS = 8; // rows, horizontal
    public static final int BOARD_FILES = 8; // columns, vertical

    public Board() {
        this.board = new Piece[BOARD_RANKS][BOARD_FILES];
    }
}