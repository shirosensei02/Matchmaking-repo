package Matchmaking;

import Matchmaking.Model.Round;
import Matchmaking.Model.RoundRepository;
import Matchmaking.Model.RoundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @InjectMocks
    private RoundService roundService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFirstRound() throws JsonProcessingException {
        Long tournamentId = 1L;

        // Hardcoded 32 players with player_id and rank in JSON format
        String playersData = "[{\"player_id\":1,\"rank\":10},{\"player_id\":2,\"rank\":8},"
                + "{\"player_id\":3,\"rank\":15},{\"player_id\":4,\"rank\":12},"
                + "{\"player_id\":5,\"rank\":6},{\"player_id\":6,\"rank\":7},"
                + "{\"player_id\":7,\"rank\":9},{\"player_id\":8,\"rank\":5},"
                + "{\"player_id\":9,\"rank\":14},{\"player_id\":10,\"rank\":13},"
                + "{\"player_id\":11,\"rank\":11},{\"player_id\":12,\"rank\":3},"
                + "{\"player_id\":13,\"rank\":2},{\"player_id\":14,\"rank\":1},"
                + "{\"player_id\":15,\"rank\":16},{\"player_id\":16,\"rank\":4},"
                + "{\"player_id\":17,\"rank\":20},{\"player_id\":18,\"rank\":19},"
                + "{\"player_id\":19,\"rank\":18},{\"player_id\":20,\"rank\":17},"
                + "{\"player_id\":21,\"rank\":21},{\"player_id\":22,\"rank\":22},"
                + "{\"player_id\":23,\"rank\":24},{\"player_id\":24,\"rank\":23},"
                + "{\"player_id\":25,\"rank\":25},{\"player_id\":26,\"rank\":26},"
                + "{\"player_id\":27,\"rank\":27},{\"player_id\":28,\"rank\":28},"
                + "{\"player_id\":29,\"rank\":29},{\"player_id\":30,\"rank\":30},"
                + "{\"player_id\":31,\"rank\":31},{\"player_id\":32,\"rank\":32}]";

        // Mock the repository's save behavior and log the saved entities
        when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> {
            Round savedRound = invocation.getArgument(0);
            // Log or print the round that would be saved to the database
            System.out.println("Saved round: " + savedRound);
            return savedRound;
        });

        // Call the service method
        List<Round> rounds = roundService.createFirstRound(tournamentId, playersData);

        // Verify that 4 rounds are created (1 round with 4 matches)
        assertEquals(4, rounds.size());

        // Verify that save was called 4 times (once for each match)
        verify(roundRepository, times(4)).save(any(Round.class));
    }

    @Test
public void testCreateNextRound() throws JsonProcessingException {
    Long tournamentId = 1L;
    int currentRound = 2;

    // Simulate the match results data (4 matches of 8 players)
    List<String> matchResultsData = new ArrayList<>();
    matchResultsData.add(
            "[{\"player_id\":1,\"rank\":10},{\"player_id\":2,\"rank\":8},{\"player_id\":3,\"rank\":15},{\"player_id\":4,\"rank\":12},{\"player_id\":5,\"rank\":6},{\"player_id\":6,\"rank\":7},{\"player_id\":7,\"rank\":9},{\"player_id\":8,\"rank\":5}]");
    matchResultsData.add(
            "[{\"player_id\":9,\"rank\":14},{\"player_id\":10,\"rank\":13},{\"player_id\":11,\"rank\":11},{\"player_id\":12,\"rank\":3},{\"player_id\":13,\"rank\":2},{\"player_id\":14,\"rank\":1},{\"player_id\":15,\"rank\":16},{\"player_id\":16,\"rank\":4}]");
    matchResultsData.add(
            "[{\"player_id\":17,\"rank\":20},{\"player_id\":18,\"rank\":19},{\"player_id\":19,\"rank\":18},{\"player_id\":20,\"rank\":17},{\"player_id\":21,\"rank\":21},{\"player_id\":22,\"rank\":22},{\"player_id\":23,\"rank\":24},{\"player_id\":24,\"rank\":23}]");
    matchResultsData.add(
            "[{\"player_id\":25,\"rank\":25},{\"player_id\":26,\"rank\":26},{\"player_id\":27,\"rank\":27},{\"player_id\":28,\"rank\":28},{\"player_id\":29,\"rank\":29},{\"player_id\":30,\"rank\":30},{\"player_id\":31,\"rank\":31},{\"player_id\":32,\"rank\":32}]");

    // Mock the repository's save behavior
    when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Call the service method for the next round
    List<Round> rounds = roundService.createNextRound(tournamentId, matchResultsData, currentRound);

    // Verify that 4 rounds are created
    assertEquals(4, rounds.size());

    // Capture the arguments passed to the save method
    ArgumentCaptor<Round> roundCaptor = ArgumentCaptor.forClass(Round.class);
    verify(roundRepository, times(4)).save(roundCaptor.capture());

    // Get all captured Round objects
    List<Round> savedRounds = roundCaptor.getAllValues();

    // Inspect and print the saved rounds
    for (Round round : savedRounds) {
        System.out.println("Captured Round:");
        System.out.println("Tournament ID: " + round.getTournamentId());
        System.out.println("Round ID: " + round.getRoundId());
        System.out.println("Match ID: " + round.getMatchId());
        System.out.println("Players Data: " + round.getPlayersData());
    }

    // Optional: Add assertions on the saved data
    assertEquals(tournamentId, savedRounds.get(0).getTournamentId());
    assertEquals(1, savedRounds.get(0).getMatchId());  // First match ID should be 1
}



    @Test
    public void testRecalibratePlayerRanks() {
        // Create a sample match with player ranks
        List<Map<String, Object>> match1 = new ArrayList<>();
        Map<String, Object> player1 = new HashMap<>();
        player1.put("player_id", 1);
        player1.put("rank", 10); // Initial rank is 10
        match1.add(player1);

        Map<String, Object> player2 = new HashMap<>();
        player2.put("player_id", 2);
        player2.put("rank", 8);
        match1.add(player2);

        // Simulate a match list with 1 match
        List<List<Map<String, Object>>> matches = new ArrayList<>();
        matches.add(match1);

        // Call recalibratePlayerRanks
        List<Map<String, Object>> recalibratedPlayers = roundService.recalibratePlayerRanks(matches);

        // Adjust the expected rank based on recalibration logic
        assertEquals(18, recalibratedPlayers.get(0).get("rank")); // Rank increased by 8 (10 + 8)
        assertEquals(15, recalibratedPlayers.get(1).get("rank")); // Rank increased by 7 (8 + 7)
    }
}