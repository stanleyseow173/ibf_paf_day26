package sg.nus.iss.ibf.workshop26.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import sg.nus.iss.ibf.workshop26.model.Game;
import sg.nus.iss.ibf.workshop26.model.Games;
import sg.nus.iss.ibf.workshop26.repository.BoardgamesRepository;

@RestController
public class BoardGamesController {
    
    BoardgamesRepository boardgamesRepository;
    BoardGamesController(BoardgamesRepository boardgamesRepository){
        this.boardgamesRepository = boardgamesRepository;
    }

    @GetMapping(path = "/games")
    public ResponseEntity<String> getAllBoardGames(@RequestParam Integer limit, @RequestParam Integer offset){
        List<Game> lstGames = boardgamesRepository.getAllGames(limit, offset);
        
        Games games = new Games();
        games.setGameList(lstGames);
        games.setOffset(offset);
        games.setLimit(limit);
        games.setTotal(lstGames.size());
        games.setTimeStamp(LocalDate.now());

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("boardgames", games.toJson());

        JsonObject result = null;
        result = objectBuilder.build();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    @GetMapping(path = "/games/rank")
    public ResponseEntity<String> getRankedBoardGames(@RequestParam Integer limit,@RequestParam Integer offset) {
        List<Game> listGames = boardgamesRepository.getSortedBoardGames(limit, offset);
    
        Games games = new Games();
        games.setGameList(listGames);
        games.setLimit(limit);
        games.setOffset(offset);
        games.setTotal(limit);
        games.setTimeStamp(LocalDate.now());
    
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("boardgames", games.toJson());
        JsonObject result = objectBuilder.build();

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
  }

  @GetMapping("/games/{gameId}")
  public ResponseEntity<String> getBoardGameById(@PathVariable Integer gameId){
    Game game = boardgamesRepository.getBoardGameById(gameId);

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("game", game.toJson());

    JsonObject result = objectBuilder.build();

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(result.toString());
  }
}
