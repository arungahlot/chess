package net.marvk.chess.board;

public class BoardState {
    private final Color activePlayer;

    private final boolean whiteCastleKing;
    private final boolean whiteCastleQueen;
    private final boolean blackCastleKing;
    private final boolean blackCastleQueen;

    private final Square enPassantTargetSquare;

    private final int halfmoveClock;
    private final int fullmoveClock;

    public BoardState(final Fen fen) {
        this.activePlayer = Color.getColorFromFen(fen.getActiveColor());

        final String castlingAvailability = fen.getCastlingAvailability();

        this.whiteCastleKing = castlingAvailability.contains(Character.toString(ColoredPiece.WHITE_KING.getSan()));
        this.whiteCastleQueen = fen.getCastlingAvailability()
                                   .contains(Character.toString(ColoredPiece.WHITE_QUEEN.getSan()));
        this.blackCastleKing = fen.getCastlingAvailability()
                                  .contains(Character.toString(ColoredPiece.BLACK_KING.getSan()));
        this.blackCastleQueen = fen.getCastlingAvailability()
                                   .contains(Character.toString(ColoredPiece.BLACK_QUEEN.getSan()));

        this.enPassantTargetSquare = Square.getSquareFromFen(fen.getEnPassantTargetSquare());

        this.halfmoveClock = Integer.parseInt(fen.getHalfmoveClock());
        this.fullmoveClock = Integer.parseInt(fen.getFullmoveClock());
    }

    private BoardState(
            final Color activePlayer,
            final boolean whiteCastleKing,
            final boolean whiteCastleQueen,
            final boolean blackCastleKing,
            final boolean blackCastleQueen,
            final Square enPassantTargetSquare,
            final int halfmoveClock,
            final int fullmoveClock
    ) {
        this.activePlayer = activePlayer;
        this.whiteCastleKing = whiteCastleKing;
        this.whiteCastleQueen = whiteCastleQueen;
        this.blackCastleKing = blackCastleKing;
        this.blackCastleQueen = blackCastleQueen;
        this.enPassantTargetSquare = enPassantTargetSquare;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveClock = fullmoveClock;
    }

    public Color getActivePlayer() {
        return activePlayer;
    }

    public boolean canWhiteCastleKing() {
        return whiteCastleKing;
    }

    public boolean canWhiteCastleQueen() {
        return whiteCastleQueen;
    }

    public boolean canBlackCastleKing() {
        return blackCastleKing;
    }

    public boolean canBlackCastleQueen() {
        return blackCastleQueen;
    }

    public Square getEnPassantTargetSquare() {
        return enPassantTargetSquare;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int getFullmoveClock() {
        return fullmoveClock;
    }

    public BoardStateBuilder nextBuilder() {
        return new BoardStateBuilder(this);
    }

    static class BoardStateBuilder {
        private final BoardState previous;

        private Boolean whiteCastleKing;
        private Boolean whiteCastleQueen;
        private Boolean blackCastleKing;
        private Boolean blackCastleQueen;

        private Square enPassantTargetSquare;

        private Integer halfmoveClock;

        public BoardStateBuilder(final BoardState previous) {
            this.previous = previous;
        }

        public BoardStateBuilder castle(final Color color) {
            if (color == Color.BLACK) {
                blackCastleKing = false;
                blackCastleQueen = false;
            } else if (color == Color.WHITE) {
                whiteCastleKing = false;
                whiteCastleQueen = false;
            }

            return this;
        }

        public BoardStateBuilder possibleEnPassant(final Square square) {
            enPassantTargetSquare = square;

            return this;
        }

        public BoardStateBuilder lostQueensideCastle(final Color color) {
            if (color == Color.BLACK) {
                blackCastleQueen = false;
            } else if (color == Color.WHITE) {
                whiteCastleQueen = false;
            }

            return this;
        }

        public BoardStateBuilder lostKingsideCastle(final Color color) {
            if (color == Color.BLACK) {
                blackCastleKing = false;
            } else if (color == Color.WHITE) {
                whiteCastleQueen = false;
            }

            return this;
        }

        public BoardStateBuilder halfmoveReset() {
            halfmoveClock = 0;

            return this;
        }

        public BoardState build() {
            return new BoardState(
                    previous.activePlayer.opposite(),
                    nullableOrElse(whiteCastleKing, previous.whiteCastleKing),
                    nullableOrElse(whiteCastleQueen, previous.whiteCastleQueen),
                    nullableOrElse(whiteCastleKing, previous.whiteCastleKing),
                    nullableOrElse(whiteCastleKing, previous.whiteCastleKing),
                    nullableOrElse(enPassantTargetSquare, previous.enPassantTargetSquare),
                    nullableOrElse(halfmoveClock, previous.halfmoveClock),
                    previous.activePlayer == Color.BLACK ? previous.fullmoveClock + 1 : previous.fullmoveClock
            );
        }
    }

    private static <T> T nullableOrElse(final T nullable, final T orElse) {
        if (nullable == null) {
            return orElse;
        }

        return nullable;
    }
}
