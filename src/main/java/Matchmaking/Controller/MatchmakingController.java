package Matchmaking.Controller;

import Matchmaking.Model.Round;
import Matchmaking.Model.RoundService;
import Matchmaking.Model.Elo.Player;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController {

    private final RoundService roundService;

    public MatchmakingController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/first-round")
    public ResponseEntity<List<List<Player>>> createFirstRound(@RequestBody Map<String, Object> payload) {
        // Call the service to create the first round with the payload
        List<List<Player>> newMatches = roundService.createFirstRound(payload);
        return ResponseEntity.ok(newMatches);
    }

    @PostMapping("/next-round")
    public ResponseEntity<List<List<Player>>> createNextRound(@RequestBody Map<String, Object> payload) {
        // Call the service to create the next round with the payload
        List<List<Player>> newMatches = roundService.createNextRound(payload);
        return ResponseEntity.ok(newMatches);
    }

}