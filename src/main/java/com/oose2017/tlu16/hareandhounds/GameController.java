package com.oose2017.tlu16.hareandhounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;


import static spark.Spark.*;

public class GameController {
    private static final String API_CONTEXT = "/hareandhounds/api";

    private final GameService gameService;

    private final Logger logger = LoggerFactory.getLogger(GameController.class);


    public GameController(GameService gameService) {
        this.gameService = gameService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(API_CONTEXT  + "/games", "application/json", (request, response) -> {
            try {
                GameInfo gameInfo = this.gameService.createNewGame(request.body());
                response.status(201);
                return gameInfo;
            } catch (GameService.GameServiceException ex) {
                response.status(400);
                logger.error(ex.getMessage());
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        put(API_CONTEXT + "/games/:id", "application/json", (request, response) -> {
            try {
                GameInfo gameInfo = this.gameService.join(request.params(":id"));
                response.status(200);
                return gameInfo;
            } catch (GameService.InvalidGameIDException ex) {
                logger.error(ex.getMessage());
                response.status(404);
                return Collections.EMPTY_MAP;
            } catch (GameService.SecondPlayerJoinedException ex) {
                logger.error(ex.getMessage());
                response.status(410);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        post(API_CONTEXT + "/games/:id/turns", "application/json", (request, response) -> {
            try {
                Game game = this.gameService.turn(request.params(":id"), request.body());
                response.status(200);
                return new PlayerID(request.params(""));
            } catch (GameService.InvalidGameIDException ex) {
                logger.error(ex.getMessage());
                response.status(404);
                return new Reason("INVALID_GAME_ID");
            } catch ( GameService.InvalidPlayerIDException ex) {
                logger.error(ex.getMessage());
                response.status(404);
                return new Reason("INVALID_PLAYER_ID");
            } catch (GameService.IncorrectTurnException ex) {
                logger.error(ex.getMessage());
                response.status(422);
                return new Reason("INCORRECT_TURN");
            } catch (GameService.InvalidMoveException ex) {
                logger.error(ex.getMessage());
                response.status(422);
                    return new Reason("ILLEGAL_MOVE");
            }
        }, new JsonTransformer());

        get(API_CONTEXT + "/games/:id/board", "application/json", (request, response) -> {
            try {
                return this.gameService.find(request.params(":id"));
            } catch (GameService.InvalidGameIDException ex) {
                logger.error(String.format("Failed to find game with id: %s", request.params(":id")));
                response.status(404);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        get(API_CONTEXT + "/games/:id/state", "application/json", (request, response)-> {
            try {
                return this.gameService.getStatus(request.params(":id"));
            } catch  (GameService.InvalidGameIDException ex) {
                logger.error(ex.getMessage());
                response.status(500);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

    }
}
