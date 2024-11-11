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
    private final ObjectMapper objectMapper;

    public RoundService(RoundRepository roundRepository, ObjectMapper objectMapper) {
        this.roundRepository = roundRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates the first round from a list of 32 players.
     * Splits players into groups of 8, assigns match IDs (1, 2, 3, 4), and stores each group.
     */
    @Transactional
    public List<List<Player>> createFirstRound(Map<String, Object> payload) {
        Long tournamentId = validateAndExtractTournamentId(payload);
        List<Player> players = extractAndValidatePlayers(payload, 32);

        // Split players into matches and store each match
        List<List<Player>> matches = splitIntoMatches(players);
        storeMatchesInRounds(tournamentId, 1, matches);

        return matches;
    }

    /**
     * Creates subsequent rounds (round 2 or 3).
     * Recalibrates player ranks, re-splits them into groups of 8, and stores each group.
     */
    @Transactional
    public List<List<Player>> createNextRound(Map<String, Object> payload) {
        Long tournamentId = validateAndExtractTournamentId(payload);
        Integer roundNumber = validateAndExtractRoundNumber(payload);
        
        List<Player> recalibratedPlayers = extractAndRecalibratePlayers(payload);

        // Re-split recalibrated players into matches and store each match
        List<List<Player>> newMatches = matchmakingAlgorithm(recalibratedPlayers);
        storeMatchesInRounds(tournamentId, roundNumber, newMatches);

        return newMatches;
    }

    private Long validateAndExtractTournamentId(Map<String, Object> payload) {
        Object tournamentIdObj = payload.get("tournamentId");
        if (tournamentIdObj == null || ((Number) tournamentIdObj).longValue() <= 0) {
            throw new IllegalArgumentException("Tournament ID must be provided and greater than zero.");
        }
        return ((Number) tournamentIdObj).longValue();
    }

    private List<Player> extractAndValidatePlayers(Map<String, Object> payload, int requiredSize) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> playersData = (List<Map<String, Object>>) payload.get("players");
        
        if (playersData == null || playersData.size() != requiredSize) {
            throw new IllegalArgumentException("Exactly " + requiredSize + " players are required.");
        }
        
        List<Player> players = new ArrayList<>();
        for (Map<String, Object> playerData : playersData) {
            players.add(mapToPlayer(playerData));
        }
        return players;
    }

    private Integer validateAndExtractRoundNumber(Map<String, Object> payload) {
        Object roundObj = payload.get("round");
        if (roundObj == null || (Integer) roundObj < 2 || (Integer) roundObj > 3) {
            throw new IllegalStateException("Invalid round number. Only rounds 2 and 3 are allowed.");
        }
        return (Integer) roundObj;
    }

    // Helper method to map a player's data to a Player object
    private Player mapToPlayer(Map<String, Object> playerData) {
        Long playerId = ((Number) playerData.get("id")).longValue();
        int rank = (int) playerData.get("rank");
        return new Player(playerId, rank);
    }

    // Helper method to extract and recalibrate players
    private List<Player> extractAndRecalibratePlayers(Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        List<List<Map<String, Object>>> playerGroups = (List<List<Map<String, Object>>>) payload.get("playerGroups");

        if (playerGroups == null || playerGroups.isEmpty()) {
            throw new IllegalArgumentException("Player groups cannot be null or empty.");
        }

        List<Player> recalibratedPlayers = new ArrayList<>();
        for (List<Map<String, Object>> group : playerGroups) {
            List<Player> match = new ArrayList<>();
            for (Map<String, Object> playerData : group) {
                match.add(mapToPlayer(playerData));
            }
            recalibratedPlayers.addAll(recalibratePlayerRanks(match));
        }
        return recalibratedPlayers;
    }

    // Helper method to split players into matches of 8
    private List<List<Player>> splitIntoMatches(List<Player> players) {
        players.sort((p1, p2) -> Integer.compare(p2.getRank(), p1.getRank()));
        return matchmakingAlgorithm(players);
    }


    private List<List<Player>> matchmakingAlgorithm(List<Player> players) {
        List<List<Player>> groups = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            groups.add(new ArrayList<>());
        }

        for (int i = 0; i < 8; i += 2) {
            for (int j = 0; j < 4; j++) {
                groups.get(j).add(players.get(i + j * 8));
                groups.get(j).add(players.get(i + j * 8 + 1));
            }
        }
        return groups;
    }

    private void storeMatchesInRounds(Long tournamentId, int roundNumber, List<List<Player>> matches) {
        for (int matchId = 1; matchId <= matches.size(); matchId++) {
            JsonNode playersJsonData = objectMapper.valueToTree(matches.get(matchId - 1));
            Round round = new Round(tournamentId, roundNumber, matchId, playersJsonData);
            roundRepository.save(round);
        }
    }

    public List<Player> recalibratePlayerRanks(List<Player> match) {
        return new Elo(match).updateRank();
    }
}
