package net.marvk.chess.ui.model;

import net.marvk.chess.core.board.Color;
import net.marvk.chess.core.board.Move;
import net.marvk.chess.core.board.MoveResult;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPlayer extends Player {
    public RandomPlayer(final Color color) {
        super(color);
    }

    @Override
    public Move play(final MoveResult previousMove) {
        final List<MoveResult> validMoves = previousMove.getBoard().generateValidMoves();

        final int index = ThreadLocalRandom.current().nextInt(validMoves.size());

        return validMoves.get(index).getMove();
    }
}
