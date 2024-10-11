package Matchmaking;

import Matchmaking.Model.Player;
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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
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

    // Create a list of 32 Player objects with player_id and rank
    List<Player> players = Arrays.asList(
        new Player(1L, 10), new Player(2L, 8), new Player(3L, 15), new Player(4L, 12),
        new Player(5L, 6), new Player(6L, 7), new Player(7L, 9), new Player(8L, 5),
        new Player(9L, 14), new Player(10L, 13), new Player(11L, 11), new Player(12L, 3),
        new Player(13L, 2), new Player(14L, 1), new Player(15L, 16), new Player(16L, 4),
        new Player(17L, 20), new Player(18L, 19), new Player(19L, 18), new Player(20L, 17),
        new Player(21L, 21), new Player(22L, 22), new Player(23L, 24), new Player(24L, 23),
        new Player(25L, 25), new Player(26L, 26), new Player(27L, 27), new Player(28L, 28),
        new Player(29L, 29), new Player(30L, 30), new Player(31L, 31), new Player(32L, 32)
    );

    // Prepare the payload with tournamentId and players
    Map<String, Object> payload = new HashMap<>();
    payload.put("tournamentId", tournamentId);
    payload.put("players", players);

    // Mock the repository's save behavior and log the saved entities
    when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> {
        Round savedRound = invocation.getArgument(0);
        // Log or print the round that would be saved to the database
        System.out.println("Saved round: " + savedRound);
        return savedRound;
    });

    // Call the service method with the payload
    List<List<Player>> matches = roundService.createFirstRound(payload);

    // Verify that 4 matches are created (4 groups of 8 players each)
    assertEquals(4, matches.size());

    // Verify that save was called 4 times (once for each match)
    verify(roundRepository, times(4)).save(any(Round.class));

    // You can also check specific details of the saved Rounds if necessary, such as matchId, roundId, etc.
}

@Test
public void testCreateNextRound() throws JsonProcessingException {
    Long tournamentId = 1L;
    int currentRound = 2;

    // Create a list of 32 Player objects with player_id and rank
    List<Player> players = Arrays.asList(
        new Player(1L, 10), new Player(2L, 8), new Player(3L, 15), new Player(4L, 12),
        new Player(5L, 6), new Player(6L, 7), new Player(7L, 9), new Player(8L, 5),
        new Player(9L, 14), new Player(10L, 13), new Player(11L, 11), new Player(12L, 3),
        new Player(13L, 2), new Player(14L, 1), new Player(15L, 16), new Player(16L, 4),
        new Player(17L, 20), new Player(18L, 19), new Player(19L, 18), new Player(20L, 17),
        new Player(21L, 21), new Player(22L, 22), new Player(23L, 24), new Player(24L, 23),
        new Player(25L, 25), new Player(26L, 26), new Player(27L, 27), new Player(28L, 28),
        new Player(29L, 29), new Player(30L, 30), new Player(31L, 31), new Player(32L, 32)
    );

    // Prepare the payload with tournamentId, players, and roundNumber
    Map<String, Object> payload = new HashMap<>();
    payload.put("tournamentId", tournamentId);
    payload.put("players", players);
    payload.put("roundNumber", currentRound);

    // Mock the repository's save behavior
    when(roundRepository.save(any(Round.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Call the service method for the next round
    List<Round> rounds = roundService.createNextRound(payload);

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
    assertEquals(currentRound, savedRounds.get(0).getRoundId());  // Round number should be 2 or 3

    // Verify the players data (optional)
    // You can add more specific checks on the "playersData" if needed
}



@Test
public void testRecalibratePlayerRanks() {
    // Create sample players for a match
    Player player1 = new Player(1L, 10); // Initial rank is 10
    Player player2 = new Player(2L, 8);  // Initial rank is 8

    // Print initial ranks
    System.out.println("Before recalibration:");
    System.out.println("Player 1 - ID: " + player1.getId() + ", Rank: " + player1.getRank());
    System.out.println("Player 2 - ID: " + player2.getId() + ", Rank: " + player2.getRank());

    // Create a match (a list of players)
    List<Player> match1 = Arrays.asList(player1, player2);

    // Simulate a list of matches (in this case, just one match with two players)
    List<List<Player>> matches = new ArrayList<>();
    matches.add(match1);

    // Call recalibratePlayerRanks to test the recalibration logic
    List<Player> recalibratedPlayers = roundService.recalibratePlayerRanks(matches);

    // Print recalibrated ranks
    System.out.println("After recalibration:");
    for (Player player : recalibratedPlayers) {
        System.out.println("Player - ID: " + player.getId() + ", New Rank: " + player.getRank());
    }

    // Check that the ranks have been updated correctly
    // Player 1 (position 0 in the match) should have rank increased by (8 - 0 = 8)
    assertEquals(18, recalibratedPlayers.get(0).getRank()); // 10 + 8 = 18
    // Player 2 (position 1 in the match) should have rank increased by (8 - 1 = 7)
    assertEquals(15, recalibratedPlayers.get(1).getRank()); // 8 + 7 = 15
}
}