package Matchmaking.Model;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import Matchmaking.Model.Elo.Elo;
import Matchmaking.Model.Elo.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper for JSON processing

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
        // Validate the payload and extract the tournamentId
        if (payload.get("tournamentId") == null) {
            throw new IllegalArgumentException("Tournament ID is missing");
        }

        Long tournamentId = ((Number) payload.get("tournamentId")).longValue();
        
        if (tournamentId <= 0) {
            throw new IllegalArgumentException("Tournament ID must be greater than zero");
        }

        // Extract and validate the players data from the payload
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> playersData = (List<Map<String, Object>>) payload.get("players");
        
        if (playersData == null || playersData.isEmpty()) {
            throw new IllegalArgumentException("Players data cannot be null or empty");
        }
        
        List<Player> players = new ArrayList<>();
        for (Map<String, Object> playerMap : playersData) {
            Long playerId = ((Number) playerMap.get("id")).longValue();
            int rank = (int) playerMap.get("rank");
            players.add(new Player(playerId, rank));
        }

        if (players.size() != 32) {
            throw new IllegalArgumentException("There must be exactly 32 players");
        }

        // Use the matchmaking algorithm to split players into groups of 8
        List<List<Player>> matches = matchmakingAlgorithm(players);

        // Store each group of 8 players into the round table with corresponding match IDs (1, 2, 3, 4)
        for (int matchId = 1; matchId <= matches.size(); matchId++) {
            // Serialize players data for each group into JSON (as JsonNode)
            JsonNode playersJsonData = objectMapper.valueToTree(matches.get(matchId - 1));

            // Create and save each match as a separate Round record
            Round round = new Round(tournamentId, 1, matchId, playersJsonData);
            roundRepository.save(round);
        }

        return matches;
    }

    /**
     * Method to create subsequent rounds (2 and 3).
     * Extracts tournamentId, players, and roundNumber from the payload,
     * recalibrates player ranks, combines players back to 32, and splits them
     * again.
     */
    @Transactional
    public List<List<Player>> createNextRound(Map<String, Object> payload) {
        // Validate the payload and extract the tournamentId
        if (payload.get("tournamentId") == null) {
            throw new IllegalArgumentException("Tournament ID is missing");
        }
        Long tournamentId = ((Number) payload.get("tournamentId")).longValue();
        
        if (tournamentId <= 0) {
            throw new IllegalArgumentException("Tournament ID must be greater than zero");
        }
    
        // Validate the round number
        if (payload.get("round") == null) {
            throw new IllegalArgumentException("Round number is missing");
        }
        Integer roundNumber = (Integer) payload.get("round");
        
        if (roundNumber < 2 || roundNumber > 3) {
            throw new IllegalStateException("Invalid round number. Only rounds 2 and 3 are allowed.");
        }
    
        // Validate and extract the playerGroups
        @SuppressWarnings("unchecked")
        List<List<Map<String, Object>>> playerGroups = (List<List<Map<String, Object>>>) payload.get("playerGroups");
        
        if (playerGroups == null || playerGroups.isEmpty()) {
            throw new IllegalArgumentException("Player groups cannot be null or empty");
        }
    
        // Create a list to hold the matches
        List<List<Player>> matches = new ArrayList<>();
    
        // Iterate through each group of players
        for (List<Map<String, Object>> group : playerGroups) {
            List<Player> playersInGroup = new ArrayList<>();
    
            // Iterate through each player in the group
            for (Map<String, Object> playerMap : group) {
                if (playerMap.get("id") == null || playerMap.get("rank") == null) {
                    throw new IllegalArgumentException("Player ID and rank cannot be null");
                }
                Long playerId = ((Number) playerMap.get("id")).longValue();
                int rank = (Integer) playerMap.get("rank");
    
                // Create a Player object and add to the playersInGroup list
                Player player = new Player(playerId, rank);
                playersInGroup.add(player);
            }
            matches.add(playersInGroup);
        }
    
        // Step 1: Recalibrate player ranks for each match in the matches
        List<Player> recalibratedPlayers = new ArrayList<>();
        for (List<Player> match : matches) {
            recalibratedPlayers.addAll(recalibratePlayerRanks(match)); // Recalibrate each match and combine players
        }
    
        // Step 2: Run the matchmaking algorithm on the recalibrated players (combine them into new groups)
        List<List<Player>> newMatches = matchmakingAlgorithm(recalibratedPlayers);
    
        // Step 3: Store each new match into the round table with new match IDs (1, 2, 3, 4)
        List<Round> rounds = new ArrayList<>();
        for (int matchId = 1; matchId <= newMatches.size(); matchId++) {
            // Convert the list of players into a JsonNode for storing in the database
            JsonNode playersJsonData = objectMapper.valueToTree(newMatches.get(matchId - 1));
    
            // Create a new Round object and save it to the database
            Round round = new Round(tournamentId, roundNumber, matchId, playersJsonData);
            rounds.add(roundRepository.save(round));
        }
    
        return newMatches; // Return the saved rounds
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
            // Return the JSON string
            return objectMapper.writeValueAsString(players);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing players to JSON", e);
        }
    }

    // Helper method to recalibrate player ranks after a round
    public List<Player> recalibratePlayerRanks(List<Player> matches) {
        Elo elo = new Elo(matches);
        return elo.updateRank();
}
}
