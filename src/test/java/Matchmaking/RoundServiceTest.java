package Matchmaking;

import Matchmaking.Entity.Player;
import Matchmaking.Repo.RoundRepository;
import Matchmaking.Entity.Round;
import Matchmaking.Service.RoundService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RoundService roundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Map<String, Object> createPlayerMap(Long id, int rank) {
        Map<String, Object> playerMap = new HashMap<>();
        playerMap.put("id", id);
        playerMap.put("rank", rank);
        return playerMap;
    }


    private List<Map<String, Object>> createPlayerMaps(int count) {
        List<Map<String, Object>> players = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            players.add(createPlayerMap(i, (int) (1000 + i)));
        }
        return players;
    }


    @Test
void testCreateFirstRound() {
    Long tournamentId = 1L;
    List<Map<String, Object>> playersData = createPlayerMaps(32);
    Map<String, Object> payload = new HashMap<>();
    payload.put("tournamentId", tournamentId);
    payload.put("players", playersData);

    JsonNode mockJsonNode = mock(JsonNode.class);
    when(objectMapper.valueToTree(any())).thenReturn(mockJsonNode);

    List<List<Player>> result = roundService.createFirstRound(payload);

    assertNotNull(result, "Result should not be null.");
    assertEquals(4, result.size(), "There should be 4 matches.");
    for (List<Player> match : result) {
        assertEquals(8, match.size(), "Each match should have 8 players.");
    }

    verify(roundRepository, times(4)).save(any(Round.class));

    verify(objectMapper, times(4)).valueToTree(any());
}


    @Test
    void testCreateFirstRound_MissingTournamentId() {
        List<Map<String, Object>> playersData = createPlayerMaps(32);
        Map<String, Object> payload = new HashMap<>();
        payload.put("players", playersData);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roundService.createFirstRound(payload);
        });

        assertEquals("Tournament ID must be provided and greater than zero.", exception.getMessage());
    }

    @Test
    void testCreateFirstRound_InvalidTournamentId() {
        List<Map<String, Object>> playersData = createPlayerMaps(32);
        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", 0L);
        payload.put("players", playersData);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roundService.createFirstRound(payload);
        });

        assertEquals("Tournament ID must be provided and greater than zero.", exception.getMessage());
    }

    @Test
    void testCreateFirstRound_WrongNumberOfPlayers() {
        Long tournamentId = 1L;
        List<Map<String, Object>> playersData = createPlayerMaps(30); // Less than 32
        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("players", playersData);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roundService.createFirstRound(payload);
        });

        assertEquals("Exactly 32 players are required.", exception.getMessage());
    }

    @Test
    void testCreateNextRound() {
        Long tournamentId = 1L;
        Integer roundNumber = 2;
        List<List<Map<String, Object>>> playerGroupsData = new ArrayList<>();

        // Create 4 groups, each with 8 players
        for (int g = 0; g < 4; g++) {
            List<Map<String, Object>> group = new ArrayList<>();
            for (int p = 0; p < 8; p++) {
                group.add(createPlayerMap((long) (g * 8 + p + 1), 1000 + g * 8 + p + 1));
            }
            playerGroupsData.add(group);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("round", roundNumber);
        payload.put("playerGroups", playerGroupsData);

        when(objectMapper.valueToTree(any())).thenReturn(mock(JsonNode.class));

        RoundService spyService = spy(roundService);
        doReturn(playerGroupsData.stream()
                .flatMap(List::stream)
                .map(playerMap -> new Player((Long) playerMap.get("id"), (int) playerMap.get("rank")))
                .toList())
                .when(spyService).recalibratePlayerRanks(anyList());

        List<List<Player>> result = spyService.createNextRound(payload);

        assertNotNull(result);
        assertEquals(4, result.size(), "There should be 4 matches.");
        for (List<Player> match : result) {
            assertEquals(8, match.size(), "Each match should have 8 players.");
        }

        verify(roundRepository, times(4)).save(any(Round.class));
    }


    @Test
    void testCreateNextRound_MissingPlayerGroups() {
        Long tournamentId = 1L;
        Integer roundNumber = 2;
        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("round", roundNumber);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roundService.createNextRound(payload);
        });

        assertEquals("Player groups cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testCreateNextRound_EmptyPlayerGroups() {
        Long tournamentId = 1L;
        Integer roundNumber = 2;
        List<List<Map<String, Object>>> playerGroupsData = new ArrayList<>(); // Empty

        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("round", roundNumber);
        payload.put("playerGroups", playerGroupsData);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roundService.createNextRound(payload);
        });

        assertEquals("Player groups cannot be null or empty.", exception.getMessage());
    }


    @Test
    void testCreateNextRound_RecalibratePlayerRanksCalled() {
        Long tournamentId = 1L;
        Integer roundNumber = 2;
        List<List<Map<String, Object>>> playerGroupsData = new ArrayList<>();

        for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
            List<Map<String, Object>> group = new ArrayList<>();
            for (long i = 1; i <= 8; i++) {
                long playerId = groupIndex * 8 + i;
                group.add(createPlayerMap(playerId, 1000 + (int) playerId));
            }
            playerGroupsData.add(group);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("round", roundNumber);
        payload.put("playerGroups", playerGroupsData);

        when(objectMapper.valueToTree(any())).thenReturn(mock(JsonNode.class));

        RoundService spyService = spy(roundService);
        List<Player> recalibratedPlayers = new ArrayList<>();
        for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
            for (int playerIndex = 0; playerIndex < 8; playerIndex++) {
                long playerId = groupIndex * 8 + playerIndex + 1;
                recalibratedPlayers.add(new Player(playerId, 1000 + (int) playerId));
            }
        }
        doReturn(recalibratedPlayers).when(spyService).recalibratePlayerRanks(anyList());

        List<List<Player>> result = spyService.createNextRound(payload);

        assertNotNull(result, "Result should not be null.");
        assertEquals(4, result.size(), "There should be 4 matches.");
        for (List<Player> match : result) {
            assertEquals(8, match.size(), "Each match should have 8 players.");
        }

        // Verify that recalibratePlayerRanks was called 4 times with any List<Player>
        verify(spyService, times(4)).recalibratePlayerRanks(anyList());

    }

    @Test
    void testCreateFirstRound_StoreMatchesInRoundsCalledCorrectly() {
        Long tournamentId = 1L;
        List<Map<String, Object>> playersData = createPlayerMaps(32);
        Map<String, Object> payload = new HashMap<>();
        payload.put("tournamentId", tournamentId);
        payload.put("players", playersData);

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.valueToTree(any())).thenReturn(mockJsonNode);

        List<List<Player>> result = roundService.createFirstRound(payload);

        ArgumentCaptor<Round> roundCaptor = ArgumentCaptor.forClass(Round.class);
        verify(roundRepository, times(4)).save(roundCaptor.capture());

        List<Round> savedRounds = roundCaptor.getAllValues();
        assertEquals(4, savedRounds.size(), "Should save 4 rounds for 4 matches.");

        for (int i = 0; i < savedRounds.size(); i++) {
            Round round = savedRounds.get(i);
            assertEquals(tournamentId, round.getTournamentId(), "Tournament ID should match.");
            assertEquals(1, round.getRoundId(), "Round number should be 1.");
            assertEquals(i + 1, round.getMatchId(), "Match ID should be sequential.");
            assertEquals(mockJsonNode, round.getPlayersData(), "Players JSON data should match.");
        }
    }

    @Test
    void testRecalibratePlayerRanks_EmptyMatch() {
        List<Player> emptyMatch = new ArrayList<>();

        List<Player> result = roundService.recalibratePlayerRanks(emptyMatch);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Recalibrated players should be empty.");
    }

}