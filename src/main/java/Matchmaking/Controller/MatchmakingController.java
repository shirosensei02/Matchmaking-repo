package Matchmaking.Controller;

import Matchmaking.Model.Round;
import Matchmaking.Model.RoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class MatchmakingController {

    private final RoundService roundService;

    public MatchmakingController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/{tournamentId}/rounds")
    public ResponseEntity<List<Round>> createRounds(@PathVariable Long tournamentId, @RequestBody String playersData) {
        // playersData would be the JSON string containing 32 players and their ranks
        List<Round> rounds = roundService.createTournamentRounds(tournamentId, playersData);
        return ResponseEntity.ok(rounds);
    }
}