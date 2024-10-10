package Matchmaking.Model;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper for JSON processing

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    /**
     * Initial method to create the first round from a list of 32 players.
     * Splits the 32 players into four groups of eight and stores them into the
     * round table.
     */
    @Transactional
    public List<Round> createFirstRound(Long tournamentId, String playersData) {
        // Parse players from the JSON input (32 players with IDs and ranks)
        List<Map<String, Object>> players = parsePlayersFromJson(playersData);

        // Use the matchmaking algorithm to split players into groups of 8
        List<List<Map<String, Object>>> matches = matchmakingAlgorithm(players);

        // Store each group of 8 players into the round table (4 rows, one for each
        // match)
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            Round round = new Round(tournamentId, 1, serializeMatchesToJson(matches.get(i))); // Round 1
            rounds.add(roundRepository.save(round));
        }

        return rounds; // Return the saved rounds
    }

    /**
     * Method to create subsequent rounds (2 and 3).
     * Recalibrates player ranks based on match results, combines players back to
     * 32, and splits them again.
     */
    @Transactional
    public List<Round> createNextRound(Long tournamentId, List<String> matchResultsData, int currentRound) {
        // Validate that the round number is either 2 or 3
        if (currentRound < 2 || currentRound > 3) {
            throw new IllegalStateException("Invalid round number. Only rounds 2 and 3 are allowed.");
        }

        // Parse the match results (4 lists of 8 players with IDs and ranks)
        List<List<Map<String, Object>>> matches = parseMatchesFromJson(matchResultsData);

        // Recalibrate player ranks based on match results
        List<Map<String, Object>> recalibratedPlayers = recalibratePlayerRanks(matches);

        // Split the recalibrated players back into 4 groups of 8
        List<List<Map<String, Object>>> newMatches = matchmakingAlgorithm(recalibratedPlayers);

        // Store each group of 8 players into the round table (4 rows for this round)
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < newMatches.size(); i++) {
            Round round = new Round(tournamentId, currentRound, serializeMatchesToJson(newMatches.get(i)));
            rounds.add(roundRepository.save(round));
        }

        return rounds; // Return the saved rounds
    }

    // Helper method to split players into 4 matches (groups of 8 players each)
    private List<List<Map<String, Object>>> matchmakingAlgorithm(List<Map<String, Object>> players) {
        List<List<Map<String, Object>>> groups = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            groups.add(new ArrayList<>(players.subList(i * 8, (i + 1) * 8)));
        }
        return groups;
    }

    // Helper method to parse JSON string into a list of player data (ID and rank)
    private List<Map<String, Object>> parsePlayersFromJson(String json) {
        try {
            // Each player is represented as a map containing "player_id" and "rank"
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error parsing players from JSON", e);
        }
    }

    // Helper method to parse match results (for subsequent rounds)
    private List<List<Map<String, Object>>> parseMatchesFromJson(List<String> jsonList) {
        try {
            List<List<Map<String, Object>>> matches = new ArrayList<>();
            for (String json : jsonList) {
                List<Map<String, Object>> match = objectMapper.readValue(json,
                        new TypeReference<List<Map<String, Object>>>() {
                        });
                matches.add(match);
            }
            return matches;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing match results from JSON", e);
        }
    }

    // Helper method to serialize matches (groups of 8 players with IDs and ranks)
    // back to JSON
    private String serializeMatchesToJson(List<Map<String, Object>> match) {
        try {
            return objectMapper.writeValueAsString(match);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing matches to JSON", e);
        }
    }

    // Helper method to recalibrate player ranks after a round
    private List<Map<String, Object>> recalibratePlayerRanks(List<List<Map<String, Object>>> matches) {
        // Update player ranks based on the results of the matches
        // Example logic (this should be replaced by your actual elo recalibration
        // logic):
        for (List<Map<String, Object>> match : matches) {
            for (int i = 0; i < match.size(); i++) {
                // Assume each player is represented as a Map with "player_id" and "rank"
                Map<String, Object> player = match.get(i);
                int currentRank = (int) player.get("rank");

                // Adjust rank based on match results (e.g., higher ranks for winners)
                player.put("rank", currentRank + (8 - i)); // This is just a placeholder logic
            }
        }

        // Combine all recalibrated players back into a single list (32 players)
        List<Map<String, Object>> recalibratedPlayers = new ArrayList<>();
        for (List<Map<String, Object>> match : matches) {
            recalibratedPlayers.addAll(match);
        }

        return recalibratedPlayers;
    }
}
