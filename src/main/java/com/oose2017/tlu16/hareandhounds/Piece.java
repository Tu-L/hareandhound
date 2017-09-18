package com.oose2017.tlu16.hareandhounds;
import java.util.Set;
import java.util.List;
interface Piece {
    /**
     * Move piece.
     * @param from where to where.
     */
    boolean turn(Turn turn);

    /**
     * Get the current position.
     * @return the current position.
     */
    int[] getPos();

    /**
     * Test whether a piece can move given other pieces positions.
     * @param opponentPos other pieces positions.
     * @return whether a piece can move.
     */
    boolean canMove(Set<String> opponentPos);
}
