package Matchmaking;

import Matchmaking.Model.Round;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {

    @Test
    public void testRoundEntity() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Long tournamentId = 1L;
        Integer roundId = 1;
        Integer matchId = 1;

        String playersDataStr = "[{\"players\": [1, 2, 3, 4, 5, 6, 7, 8]}]";

        JsonNode playersData = objectMapper.readTree(playersDataStr);

        Round round = new Round(tournamentId, roundId, matchId, playersData);

        // Test getters
        assertEquals(tournamentId, round.getTournamentId());
        assertEquals(roundId, round.getRoundId());
        assertEquals(matchId, round.getMatchId());
        assertEquals(playersData, round.getPlayersData());

        // Test setters
        round.setRoundId(2);
        assertEquals(2, round.getRoundId());

        round.setMatchId(3);
        assertEquals(3, round.getMatchId());

        // New JSON String for updating playersData
        String newPlayersDataStr = "[{\"players\": [9, 10, 11, 12, 13, 14, 15, 16]}]";
        JsonNode newPlayersData = objectMapper.readTree(newPlayersDataStr);

        // Test playersData setter
        round.setPlayersData(newPlayersData);
        assertEquals(newPlayersData, round.getPlayersData());
    }
}