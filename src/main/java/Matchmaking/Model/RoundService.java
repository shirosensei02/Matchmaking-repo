package Matchmaking.Model;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    /**
     * Initial method to create the first round from a list of 32 players.
     * Extracts tournamentId and a list of Player objects from the payload,
     * splits players into groups of 8, assigns match IDs (1, 2, 3, 4),
     * and stores each group into the round table.
     */
    @Transactional
    public List<List<Player>> createFirstRound(Map<String, Object> payload) {
        // Extract the tournamentId and players data from the payload
        Long tournamentId = (Long) payload.get("tournamentId");
        @SuppressWarnings("unchecked")
        List<Player> players = (List<Player>) payload.get("players");

        // Use the matchmaking algorithm to split players into groups of 8
        List<List<Player>> matches = matchmakingAlgorithm(players);

        // Store each group of 8 players into the round table with corresponding match
        // IDs (1, 2, 3, 4)
        for (int matchId = 1; matchId <= matches.size(); matchId++) {
            // Serialize players data for each group into JSON (optional based on DB
            // structure)
            String playersData = serializePlayersToJson(matches.get(matchId - 1));

            // Create and save each match as a separate Round record
            Round round = new Round(tournamentId, 1, matchId, playersData); // Round 1, Match ID 1-4
            roundRepository.save(round);
        }

        // Return the groups of 8 players (List<List<Player>>)
        return matches;
    }

    /**
     * Method to create subsequent rounds (2 and 3).
     * Extracts tournamentId, players, and roundNumber from the payload,
     * recalibrates player ranks, combines players back to 32, and splits them
     * again.
     */
    @Transactional
    public List<Round> createNextRound(Map<String, Object> payload) {
        // Extract tournamentId, matches (List<List<Player>>), and roundNumber from the
        // payload
        Long tournamentId = (Long) payload.get("tournamentId");
        @SuppressWarnings("unchecked")
        List<List<Player>> matches = (List<List<Player>>) payload.get("players"); // Now a list of lists
        Integer roundNumber = (Integer) payload.get("roundNumber");

        // Validate that the round number is either 2 or 3
        if (roundNumber < 2 || roundNumber > 3) {
            throw new IllegalStateException("Invalid round number. Only rounds 2 and 3 are allowed.");
        }

        // Step 1: Recalibrate player ranks for each match in the matches
        List<Player> recalibratedPlayers = new ArrayList<>();
        for (List<Player> match : matches) {
            recalibratedPlayers.addAll(recalibratePlayerRanks(match)); // Recalibrate each match and combine players
        }

        // Step 2: Run the matchmaking algorithm on the recalibrated players (combine
        // them into new groups)
        List<List<Player>> newMatches = matchmakingAlgorithm(recalibratedPlayers);

        // Step 3: Store each new match into the round table with new match IDs (1, 2,
        // 3, 4)
        List<Round> rounds = new ArrayList<>();
        for (int matchId = 1; matchId <= newMatches.size(); matchId++) {
            String playersData = serializePlayersToJson(newMatches.get(matchId - 1)); // Serialize players in each group
            Round round = new Round(tournamentId, roundNumber, matchId, playersData);
            rounds.add(roundRepository.save(round));
        }

        return rounds; // Return the saved rounds
    }

    // Helper method to split players into 4 groups based on their ranking
    private List<List<Player>> matchmakingAlgorithm(List<Player> players) {
        // Step 1: Sort players by their rank in descending order
        players.sort((p1, p2) -> Integer.compare(p2.getRank(), p1.getRank()));

        // Step 2: Distribute players into 4 groups in a round-robin fashion
        List<List<Player>> groups = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            groups.add(new ArrayList<>()); // Initialize 4 groups
        }

        for (int i = 0; i < players.size(); i++) {
            // Distribute players across 4 groups
            groups.get(i % 4).add(players.get(i));
        }

        return groups;
    }

    // Helper method to serialize players (list of Player objects) back to JSON
    private String serializePlayersToJson(List<Player> players) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(players);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing players to JSON", e);
        }
    }

    // Helper method to recalibrate player ranks after a round
    private List<Player> recalibratePlayerRanks(List<Player> matches) {
        Elo elo = new Elo(matches);
        return elo.updateRank();
    }
}
