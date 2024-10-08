package Matchmaking.Model;

import Matchmaking.Model.Round;
import Matchmaking.Model.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    // Main method to create all rounds
    public List<Round> createTournamentRounds(Long tournamentId, String playersData) {
        List<Round> rounds = new ArrayList<>();
        List<String> players = parsePlayersFromJson(playersData); // Convert JSON to list of 32 players

        for (int roundNumber = 1; roundNumber <= 3; roundNumber++) {
            // Apply matchmaking algorithm for each round
            List<List<String>> groups = matchmakingAlgorithm(players); // 4 groups of 8 players

            // Create JSON structure for the 4 matches
            String matchesJson = serializeMatchesToJson(groups);

            // Create and save the round
            Round round = new Round(tournamentId, roundNumber, matchesJson);
            rounds.add(roundRepository.save(round));

            // After each round, recalibrate ranks based on match results
            players = recalibratePlayerRanks(groups);
        }

        return rounds;
    }

    // Helper method to split players into 4 matches (groups of 8 players each)
    private List<List<String>> matchmakingAlgorithm(List<String> players) {
        // Your custom matchmaking algorithm logic
        List<List<String>> groups = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            groups.add(new ArrayList<>(players.subList(i * 8, (i + 1) * 8)));
        }
        return groups;
    }

    // Helper method to parse JSON string into list of players
    private List<String> parsePlayersFromJson(String json) {
        // Use Jackson or Gson to parse the JSON
        return new ArrayList<>(); // Placeholder for actual parsing logic
    }

    // Helper method to serialize matches (groups of 8 players) back to JSON
    private String serializeMatchesToJson(List<List<String>> matches) {
        // Use Jackson or Gson to serialize the groups (matches) into a JSON string
        return matches.toString(); // Placeholder for actual serialization logic
    }

    // Helper method to recalibrate player ranks after a round
    private List<String> recalibratePlayerRanks(List<List<String>> matches) {
        // Update player ranks based on the results of the matches
        // Placeholder logic for recalibration
        return new ArrayList<>(); // Return updated list of 32 players
    }
}