package Matchmaking.Model;

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
            // Apply matchmaking algorithm for each round to split players into 4 groups
            List<List<String>> groups = matchmakingAlgorithm(players); // 4 groups of 8 players

            for (int matchId = 1; matchId <= 4; matchId++) {
                // For each group (match), serialize the players in that match to JSON
                String matchJson = serializeMatchesToJson(groups.get(matchId - 1));

                // Create and save each round with a specific match ID
                Round round = new Round(tournamentId, roundNumber, matchId, matchJson);
                rounds.add(roundRepository.save(round));  // Save each match as a separate entry
            }

            // After each round, recalibrate ranks based on match results
            players = recalibratePlayerRanks(groups);
        }

        return rounds;
    }

    // Helper method to split players into 4 matches (groups of 8 players each)
    public List<List<String>> matchmakingAlgorithm(List<String> players) {
        List<List<String>> groups = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            groups.add(new ArrayList<>(players.subList(i * 8, (i + 1) * 8)));
        }
        return groups;
    }

    // Helper method to parse JSON string into list of players
    public List<String> parsePlayersFromJson(String json) {
        // Use Jackson or Gson to parse the JSON
        return new ArrayList<>(); // Placeholder for actual parsing logic
    }

    // Helper method to serialize matches (groups of 8 players) back to JSON
    public String serializeMatchesToJson(List<String> match) {
        // Use Jackson or Gson to serialize the match (list of 8 players) into a JSON string
        return match.toString(); // Placeholder for actual serialization logic
    }

    // Helper method to recalibrate player ranks after a round
    public List<String> recalibratePlayerRanks(List<List<String>> matches) {
        // Update player ranks based on the results of the matches
        return new ArrayList<>(); // Return updated list of 32 players
    }
}