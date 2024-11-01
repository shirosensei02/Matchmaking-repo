package Matchmaking;

import Matchmaking.Controller.MatchmakingController;
import Matchmaking.Model.Elo.Player;
import Matchmaking.Model.Round;
import Matchmaking.Model.RoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundControllerTest {

    @Mock
    private RoundService roundService;

    @InjectMocks
    private MatchmakingController matchmakingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void testCreateFirstRound() {
        Long tournamentId = 1L;
        List<Player> group1 = Arrays.asList(new Player(1L, 10), new Player(2L, 8), new Player(3L, 15), new Player(4L, 12));
        List<Player> group2 = Arrays.asList(new Player(5L, 6), new Player(6L, 7), new Player(7L, 9), new Player(8L, 5));
        List<List<Player>> mockMatches = Arrays.asList(group1, group2);

        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("players", mockMatches);

        when(roundService.createFirstRound(any())).thenReturn(mockMatches);

        ResponseEntity<List<List<Player>>> response = matchmakingController.createFirstRound(payload);

        assertEquals(200, response.getStatusCodeValue());  // Ensure 200 OK response
        assertNotNull(response.getBody());  // Ensure body is not null
        assertEquals(2, response.getBody().size());  // Ensure there are 2 groups (matches)
        
        verify(roundService, times(1)).createFirstRound(payload);
    }

    @Test
    public void testCreateNextRound() {
        Long tournamentId = 1L;
        List<Player> group1 = Arrays.asList(new Player(1L, 10), new Player(2L, 8));
        List<Player> group2 = Arrays.asList(new Player(3L, 15), new Player(4L, 12));
        List<List<Player>> mockMatches = Arrays.asList(group1, group2);

        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("round", 2);
        payload.put("playerGroups", mockMatches);

        when(roundService.createNextRound(any())).thenReturn(mockMatches);

        ResponseEntity<List<List<Player>>> response = matchmakingController.createNextRound(payload);

        assertEquals(200, response.getStatusCodeValue());  // Ensure 200 OK response
        assertNotNull(response.getBody());  // Ensure body is not null
        assertEquals(2, response.getBody().size());  // Ensure there are 2 groups (matches)

        verify(roundService, times(1)).createNextRound(payload);
    }
}