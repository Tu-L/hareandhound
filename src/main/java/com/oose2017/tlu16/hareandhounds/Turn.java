package com.oose2017.tlu16.hareandhounds;

public class Turn {
    private String playerId;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;
    public Turn(String playerId, int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.playerId = playerId;
    }

    /**
     * Get the player ID.
     * @return the player ID.
     */
    public String getPlayerId() {
        return this.playerId;
    }

    /**
     * Get the from position.
     * @return the from position.
     */
    public int[] getFromPos() {
        int[] res = new int[2];
        res[0] = fromX;
        res[1] = fromY;
        return res;
    }

    /**
     * Get the to position.
     * @return the to position.
     */
    public int[] getToPos() {
        int[] res = new int[2];
        res[0] = toX;
        res[1] = toY;
        return res;
    }

    public String toString() {
        return playerId + ": " + fromX+ "," + fromY + ";" + toX + "," + toY;
    }
}
