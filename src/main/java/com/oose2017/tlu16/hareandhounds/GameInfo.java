package com.oose2017.tlu16.hareandhounds;

public class GameInfo {
    private String gameId;
    private String playerId;
    private String pieceType;
    public GameInfo(String gameId, String playerId, String pieceType) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.pieceType = pieceType;
    }
    public String getPieceType() {
        return this.pieceType;
    }
}
