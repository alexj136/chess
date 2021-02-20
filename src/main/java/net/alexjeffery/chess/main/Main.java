package net.alexjeffery.chess.main;

import net.alexjeffery.chess.game.Board;
import net.alexjeffery.chess.game.Piece;

public class Main {

    public static void main(String[] args) {
        System.out.println(new Board(Board.START_FEN_STRING));
        System.out.println(new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));
        System.out.println(new Board("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2"));
        System.out.println(new Board("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2"));

    }
}
