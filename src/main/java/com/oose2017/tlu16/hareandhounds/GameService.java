package com.oose2017.tlu16.hareandhounds;

import com.google.gson.Gson;

import java.util.*;

public class GameService {
    private HashMap<Integer, Game> dataSet;
    private static int ID = 0;

    public GameService() {
        this.dataSet = new HashMap<>();
    }

    public GameInfo createNewGame(String body) throws GameServiceException {
        GameInfo gameInfo = new Gson().fromJson(body, GameInfo.class);
        if (gameInfo == null) {
            throw new GameServiceException("Bad request");
        }
        String pieceType = gameInfo.getPieceType();
        if (pieceType == null || (!pieceType.equals("HARE") && !pieceType.equals("HOUND"))) {
            throw new GameServiceException("Bad request");
        }
        Game game = new Game(pieceType);
        int id = ID;
        this.dataSet.put(ID++, game);
        return new GameInfo(String.valueOf(id), String.valueOf(0), pieceType);
    }

    public GameInfo join(String ID) throws InvalidGameIDException, SecondPlayerJoinedException {
        Game game = get(ID);
        if (!game.isAvailable()) {
            throw new SecondPlayerJoinedException("Second player already joined.");
        }
        String pieceType = game.joinGame();
        return new GameInfo(ID, "1", pieceType);
    }

    private Game get(String ID) throws InvalidGameIDException {
        try {
            int id = Integer.parseInt(ID);
            if (!this.dataSet.containsKey(id)) {
                throw new InvalidGameIDException("Cannot find game with id " + ID);
            }
            return this.dataSet.get(id);
        } catch (NumberFormatException ex) {
            throw new InvalidGameIDException("Cannot find game with id " + ID);
        }

    }

    public Game turn(String ID, String body) throws InvalidGameIDException,
            InvalidPlayerIDException, IncorrectTurnException, InvalidMoveException {
        Game game = this.get(ID);
        Turn turn = new Gson().fromJson(body, Turn.class);
        if (!game.containsPlayer(turn.getPlayerId())) {
            throw new InvalidPlayerIDException("There's no such player.");
        }
        if (!game.isRightTurn(turn.getPlayerId())) {
            throw new IncorrectTurnException("It is not the player's turn.");
        }
        if (turn == null || !game.turn(turn)) {
            throw new InvalidMoveException("Invalid move.");
        }
        return game;
    }

    public List<Piece> find(String ID) throws InvalidGameIDException {
//        return this.get(ID).getBoard();
        List<Piece> tmp = new ArrayList<>(this.get(ID).getBoard());
        Collections.reverse(tmp);
        return tmp;
    }

    public State getStatus(String ID) throws InvalidGameIDException {
        return this.get(ID).getStatus();
    }
    //-----------------------------------------------------------------------------//
    // Helper Classes
    //-----------------------------------------------------------------------------//

    public static class GameServiceException extends Exception {
        public GameServiceException(String message) {
            super(message);
        }
    }

    public static class InvalidGameIDException extends Exception {
        public InvalidGameIDException(String message) {
            super(message);
        }
    }

    public static class SecondPlayerJoinedException extends Exception {
        public SecondPlayerJoinedException(String message) {
            super(message);
        }
    }

    public static class InvalidPlayerIDException extends Exception {
        public InvalidPlayerIDException(String message) {
            super(message);
        }
    }

    public static class IncorrectTurnException extends Exception {
        public IncorrectTurnException(String message) {
            super(message);
        }
    }

    public static class InvalidMoveException extends Exception {
        public InvalidMoveException(String message) {
            super(message);
        }
    }
}
