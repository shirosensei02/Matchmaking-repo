package Matchmaking;

import Matchmaking.Model.Round;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {

    @Test
    public void testRoundEntity() {
        // Sample data
        Long tournamentId = 1L;
        Integer roundId = 1;
        Integer matchId = 1;
        String playersData = "[{\"players\": [1, 2, 3, 4, 5, 6, 7, 8]}]";

        // Create a new Round instance
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

        round.setPlayersData("[{\"players\": [9, 10, 11, 12, 13, 14, 15, 16]}]");
        assertEquals("[{\"players\": [9, 10, 11, 12, 13, 14, 15, 16]}]", round.getPlayersData());
    }
}