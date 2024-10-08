package Matchmaking;

import Matchmaking.Model.Round;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {

    @Test
    public void testRoundEntity() {
        // Sample data
        Long tournamentId = 1L;
        Integer roundNumber = 1;
        String matches = "[{\"players\": [1, 2, 3, 4, 5, 6, 7, 8]}]";

        // Create a new Round instance
        Round round = new Round(tournamentId, roundNumber, matches);

        // Test getters
        assertEquals(tournamentId, round.getTournamentId());
        assertEquals(roundNumber, round.getRoundNumber());
        assertEquals(matches, round.getMatches());

        // Test setters
        round.setRoundNumber(2);
        assertEquals(2, round.getRoundNumber());
    }
}
