package Matchmaking;

import Matchmaking.Model.Elo.Player;
import Matchmaking.Model.Round;
import Matchmaking.Model.RoundRepository;
import Matchmaking.Model.RoundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @InjectMocks
    private RoundService roundService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFirstRound() throws JsonProcessingException {
        Long tournamentId = 1L;

        // Prepare the list of players (32 players)
        List<Map<String, Object>> players = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            Map<String, Object> player = new HashMap<>();
            player.put("id", (long) i);  // Player IDs are long
            player.put("rank", i);  // Assigning rank as i for simplicity
            players.add(player);
        }

        // Prepare the payload with tournamentId and players
        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("players", players);

        // Mock repository save behavior
        when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method with the payload
        List<List<Player>> matches = roundService.createFirstRound(payload);

        // Verify that 4 matches (groups of 8 players each) are created
        assertEquals(4, matches.size());

        // Verify that save was called 4 times (once for each match)
        verify(roundRepository, times(4)).save(any(Round.class));

        // Capture the arguments passed to the save method
        ArgumentCaptor<Round> roundCaptor = ArgumentCaptor.forClass(Round.class);
        verify(roundRepository, times(4)).save(roundCaptor.capture());

        // Check the saved rounds for consistency
        List<Round> savedRounds = roundCaptor.getAllValues();
        assertEquals(4, savedRounds.size());  // Ensure 4 rounds are saved
        for (Round round : savedRounds) {
            assertNotNull(round.getPlayersData());  // Ensure players data is not null
            assertEquals(tournamentId, round.getTournamentId());  // Verify the tournamentId
        }
    }

    @Test
    public void testCreateNextRound() throws JsonProcessingException {
        Long tournamentId = 1L;
        int currentRound = 2;

        // Prepare hardcoded matches with players
        List<Map<String, Object>> match1 = createPlayerList(1, 8);
        List<Map<String, Object>> match2 = createPlayerList(9, 16);
        List<Map<String, Object>> match3 = createPlayerList(17, 24);
        List<Map<String, Object>> match4 = createPlayerList(25, 32);

        // Combine the matches into a list
        List<List<Map<String, Object>>> playerGroups = List.of(match1, match2, match3, match4);

        // Prepare the payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("playerGroups", playerGroups);
        payload.put("round", currentRound);

        // Mock repository save behavior
        when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        List<List<Player>> newMatches = roundService.createNextRound(payload);

        // Verify that 4 rounds are created
        assertEquals(4, newMatches.size());

        // Capture saved rounds
        ArgumentCaptor<Round> roundCaptor = ArgumentCaptor.forClass(Round.class);
        verify(roundRepository, times(4)).save(roundCaptor.capture());

        // Verify the saved rounds
        List<Round> savedRounds = roundCaptor.getAllValues();
        for (Round round : savedRounds) {
            assertEquals(tournamentId, round.getTournamentId());
            assertNotNull(round.getPlayersData());  // Ensure players data is saved as JSON
        }
    }

    @Test
    public void testRecalibratePlayerRanks() {
        // Sample players before recalibration
        Player player1 = new Player(1L, 10); // Initial rank 10
        Player player2 = new Player(2L, 8);  // Initial rank 8

        List<Player> match = List.of(player1, player2);

        // Recalibrate player ranks using the service method
        List<Player> recalibratedPlayers = roundService.recalibratePlayerRanks(match);

        // Assuming the Elo system adjusts ranks based on performance
        // In this test, we'll just print out the recalibrated ranks
        System.out.println("After recalibration:");
        for (Player player : recalibratedPlayers) {
            System.out.println("Player - ID: " + player.getId() + ", New Rank: " + player.getRank());
        }

        // Assertions can be based on expected behavior after recalibration
        // For now, the printed output will help debug rank adjustments
        // assertEquals(expectedRank1, recalibratedPlayers.get(0).getRank());
        // assertEquals(expectedRank2, recalibratedPlayers.get(1).getRank());
    }

    // Helper method to create a list of players
    private List<Map<String, Object>> createPlayerList(int startId, int endId) {
        List<Map<String, Object>> players = new ArrayList<>();
        for (int i = startId; i <= endId; i++) {
            Map<String, Object> player = new HashMap<>();
            player.put("id", (long) i);  // Player IDs are long
            player.put("rank", i);  // Assigning rank as i for simplicity
            players.add(player);
        }
        return players;
    }
}