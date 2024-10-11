package Matchmaking.Controller;

import Matchmaking.Model.Round;
import Matchmaking.Model.RoundService;
import Matchmaking.Model.Player.Player;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tournaments")
public class MatchmakingController {

    private final RoundService roundService;

    public MatchmakingController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/{tournamentId}/first-round")
    public ResponseEntity<List<List<Player>>> createFirstRound(@RequestBody Map<String, Object> payload) {
        // Call the service to create the first round with the payload
        List<List<Player>> newMatches = roundService.createFirstRound(payload);
        return ResponseEntity.ok(newMatches);
    }

    @PostMapping("/{tournamentId}/next-round")
    public ResponseEntity<List<Round>> createNextRound(@RequestBody Map<String, Object> payload) {
        // Call the service to create the next round with the payload
        List<Round> newRounds = roundService.createNextRound(payload);
        return ResponseEntity.ok(newRounds);
    }

}