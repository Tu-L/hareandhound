//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.tlu16.hareandhounds;
import java.util.*;
public class Game {

    private List<Piece> pieceList;
    private Map<String, Integer> conditions;
    private boolean isHoundTurn;
    String[] players = new String[2];

    public Game(String pieceType) {
        players[0] = pieceType;
        this.pieceList = new ArrayList<>();
        this.conditions = new HashMap<>();
        this.isHoundTurn = true;
    }

//    /**
//     * Get game ID.
//     * @return game ID.
//     */
//    public String getGameId() {return this.gameId;}
//
//    /**
//     * Get player ID.
//     * @return player ID.
//     */
//    public String getPlayerId() {
//        return this.playerId;
//    }
//
//    /**
//     * Get piece type.
//     * @return the piece type.
//     */
//    public String getPieceType() {
//        return this.pieceType;
//    }

//    @Override
//    public boolean equals(Object that) {
//        if (this == that) return true;
//        if (that == null || getClass() != that.getClass()) return false;
//
//        Game game = (Game) that;
//
//        if (this.gameId != null ? !this.gameId.equals(game.gameId) : game.gameId != null) return false;
//        if (this.playerId != null ? !this.playerId.equals(game.playerId) : game.playerId != null) return false;
//        return !(this.pieceType != null ? !this.pieceType.equals(game.pieceType) : game.pieceType != null);
//
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = this.gameId != null ? this.gameId.hashCode() : 0;
//        result = 31 * result + (this.playerId != null ? this.playerId.hashCode() : 0);
//        result = 31 * result + (this.pieceType != null ? this.pieceType.hashCode() : 0);
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        return "Game{" +
//                "gameId='" + this.gameId + '\'' +
//                ", playerId='" + this.playerId + '\'' +
//                ", pieceType=" + this.pieceType +
//                '}';
//    }

    /**
     * Add pieces to the board.
     */
    public String joinGame() {
        this.pieceList.add(new Hare());
        this.pieceList.add(new Hound(1, 0));
        this.pieceList.add(new Hound(0,1));
        this.pieceList.add(new Hound(1,2));
        this.conditions.put("41 1 10 12", 1);
        if (this.players[0].equals("HARE")) {
            this.players[1] = "HOUND";
        } else {
            this.players[1] = "HARE";
        }
        return this.players[1];
    }

    /**
     * Check whether the game can be joined.
     * @return whether the game can be joined.
     */
    public boolean isAvailable() {
        return this.players[1] == null;
    }

    /**
     * Get the positions of the four pieces, i.e. the board.
     * @return the board condition.
     */
    public List<Piece> getBoard() {
        return this.pieceList;
    }

    /**
     * Get the current status of the game.
     * @return the current status of the game.
     */
    public State getStatus() {
        if (this.isAvailable()) {
            return new State("WAITING_FOR_SECOND_PLAYER");
        }
        if (this.isHoundTurn && this.hareEscaped()) {
            return new State("WIN_HARE_BY_ESCAPE");
        }
        if (!this.isHoundTurn && this.houndsWon()) {
            return new State("WIN_HOUND");
        }
        if (!this.isHoundTurn && this.hasStalling()) {
            return new State("WIN_HARE_BY_STALLING");
        }
        if (this.isHoundTurn) {
            return new State("TURN_HOUND");
        }
        return new State("TURN_HARE");
    }

    /**
     * Determine whether hare has escaped.
     * @return whether there's no hound to the left of the hare.
     */
    private boolean hareEscaped() {
        Piece hare = this.pieceList.get(0);
        for (int i = 1; i < 4; i++) {
            if (this.pieceList.get(i).getPos()[0] < hare.getPos()[0]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine whether hounds have won by checking whether the hare has valid move.
     * @return whether hounds have won.
     */
    private boolean houndsWon() {
        Set<String> houndsPos = new HashSet<>();
        for (int i = 1; i < 4; i++) {
            StringBuilder sb = new StringBuilder();
            int[] tmp = this.pieceList.get(i).getPos();
            sb.append(tmp[0] * 10 + tmp[1]);
            houndsPos.add(sb.toString());
        }
        return !this.pieceList.get(0).canMove(houndsPos);
    }

    /**
     * Determine whether the same condition has happened 3 or more times.
     * @return whether the same condition has happened 3 or more times.
     */
    private boolean hasStalling() {
        for (Map.Entry<String, Integer> me : this.conditions.entrySet()) {
            if (me.getValue() >= 3) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPlayer(String ID) {
        try {
            int id = Integer.valueOf(ID);
            return id < 2;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean isRightTurn(String playerId) {
        int id = Integer.valueOf(playerId);
        return this.players[id].equals("HARE") ^ this.isHoundTurn;
    }

    public boolean turn(Turn turn) {
        String[] poses = this.getPos().split(" ");
        String toPos = String.valueOf(turn.getToPos()[0] * 10 + turn.getToPos()[1]);
        for (int i = 0; i < poses.length; i++) {
            if (poses[i].equals(toPos)) {
                return false;
            }
        }
        int id = Integer.valueOf(turn.getPlayerId());
        if (this.players[id].equals("HARE")) {
            Piece hare = this.pieceList.get(0);
            if (hare.turn(turn)) {
                this.updateCondition();
                this.isHoundTurn = true;
                return true;
            }
            return false;
        }
        for (int i = 1; i < 4; i++) {
            Piece hound = this.pieceList.get(i);
            if (hound.turn(turn)) {
                this.updateCondition();
                this.isHoundTurn = false;
                return true;
            }
        }
        return false;
    }

    private String getPos() {
        int[] houndPos = new int[3];
        for (int i = 1; i < 4; i++) {
            Piece hound = this.pieceList.get(i);
            houndPos[i - 1] = hound.getPos()[0] * 10 + hound.getPos()[1];
        }
        Arrays.sort(houndPos);
        StringBuilder sb = new StringBuilder();
        sb.append(this.pieceList.get(0).getPos()[0] * 10 + this.pieceList.get(0).getPos()[1]);
        for (int i = 0; i < 3; i++) {
            sb.append(" ");
            sb.append(houndPos[i]);
        }
        return sb.toString();
    }
    private void updateCondition() {
        String currCon = this.getPos();
        if (!this.conditions.containsKey(currCon)) {
            this.conditions.put(currCon, 0);
        }
        this.conditions.put(currCon, this.conditions.get(currCon) + 1);
    }
}
