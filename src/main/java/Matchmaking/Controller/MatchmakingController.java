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

    @PostMapping("/{tournamentId}/next-round")
    public ResponseEntity<Round> createNextRound(@PathVariable Long tournamentId,
            @RequestBody String playersData,
            @RequestParam int currentRound) {
        // Call the service to create the next round
        Round newRound = roundService.createNextTournamentRound(tournamentId, playersData, currentRound);
        return ResponseEntity.ok(newRound);
    }
}
