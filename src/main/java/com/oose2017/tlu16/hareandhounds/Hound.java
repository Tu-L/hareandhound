package com.oose2017.tlu16.hareandhounds;
import java.util.Map;
import java.util.Set;

public class Hound implements Piece{
    String pieceType;
    int x;
    int y;
    public Hound (int x, int y) {
        this.pieceType = "HOUND";
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean turn(Turn turn) {
        Map<Integer, Set<Integer>> moves = Board.HOUNDMOVES;
        int[] fromPos = turn.getFromPos();
        if (fromPos[0] != this.x || fromPos[1] != this.y) {
            return false;
        }
        int[] toPos = turn.getToPos();
        if (!moves.get(fromPos[0] * 10 + fromPos[1]).contains(toPos[0] * 10 + toPos[1])) {
            return false;
        }
        this.x = toPos[0];
        this.y = toPos[1];
        return true;
    }

    @Override
    public int[] getPos() {
        int[] pos = new int[2];
        pos[0] = this.x;
        pos[1] = this.y;
        return pos;
    }

    @Override
    public boolean canMove(Set<String> opponentsPos) {
        if ((this.x + this.y) % 2 == 1) {
            int[] dx = {0, 0, 1, 1, 1};
            int[] dy = {-1, 1, -1, 0, 1};
            for (int i = 0; i < 5; i++) {
                int nx = this.x + dx[i];
                int ny = this.y + dy[i];
                if (isValid(nx, ny)) {
                    if (!opponentsPos.contains(String.valueOf(nx * 10 + ny))) {
                        return true;
                    }
                }
            }
            return false;
        }
        int[] dx = {0, 0, 1};
        int[] dy = {-1, 1, 0};
        for (int i = 0; i < 3; i++) {
            int nx = this.x + dx[i];
            int ny = this.y + dy[i];
            if (isValid(nx, ny)) {
                if (!opponentsPos.contains(String.valueOf(nx * 10 + ny))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Test whether the coordinate is within th he board.
     * @param x x coordinate.
     * @param y y coordinate.
     * @return whether the coordinate is within the board.
     */
    private boolean isValid(int x, int y) {
        if (y == 0) {
            return x >= 1 && x <= 3;
        }
        if (y == 1) {
            return x >= 0 && x <= 4;
        }
        if (y == 2) {
            return x >= 1 && x <= 3;
        }
        return false;
    }
}
