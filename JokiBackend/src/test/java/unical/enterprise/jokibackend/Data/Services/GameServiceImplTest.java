package unical.enterprise.jokibackend.Data.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;
import unical.enterprise.jokibackend.Data.Dao.GameDao;
import unical.enterprise.jokibackend.Data.Dto.AdminDto;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Services.Interfaces.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceImplTest {

    @Mock
    private AdminService adminService;

    @Mock
    private GameDao gameDao;

    @Mock
    AdminDto adminDto;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGameById() {
        UUID id = UUID.randomUUID();
        Game game = new Game();
        GameDto gameDto = new GameDto();

        when(gameDao.findById(id)).thenReturn(Optional.of(game));
        when(modelMapper.map(game, GameDto.class)).thenReturn(gameDto);

        GameDto result = gameService.getGameById(id);

        assertNotNull(result);
        assertEquals(gameDto, result);
    }

    @Test
    void testGetGameById_NotFound() {
        UUID id = UUID.randomUUID();

        when(gameDao.findById(id)).thenReturn(Optional.empty());

        GameDto result = gameService.getGameById(id);

        assertNull(result);
    }

    @Test
    void testFindAll() {
        List<Game> games = Arrays.asList(new Game(), new Game());
        List<GameDto> gameDtos = Arrays.asList(new GameDto(), new GameDto());

        when(gameDao.findAll()).thenReturn(games);
        when(modelMapper.map(any(Game.class), eq(GameDto.class))).thenReturn(gameDtos.get(0), gameDtos.get(1));

        Collection<GameDto> result = gameService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindAll_Empty() {
        when(gameDao.findAll()).thenReturn(Collections.emptyList());

        Collection<GameDto> result = gameService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSave() {
        GameDto gameDto = new GameDto();
        gameDto.setTitle("Test Title"); // Assicurati che il titolo non sia null
        Game game = new Game();
        GameDto savedGameDto = new GameDto();
        AdminDto admin = new AdminDto();
        when(adminService.getByUsername(anyString())).thenReturn(admin);
        when(modelMapper.map(gameDto, Game.class)).thenReturn(game);
        when(gameDao.save(game)).thenReturn(game);
        when(modelMapper.map(game, GameDto.class)).thenReturn(savedGameDto);

        GameDto result = gameService.save(gameDto);

        assertNotNull(result);
        assertEquals(savedGameDto, result);
    }

    @Test
    void testSave_Failure() {
        GameDto gameDto = new GameDto();
        AdminDto admin = new AdminDto();

        when(adminService.getByUsername(anyString())).thenReturn(admin);
        when(modelMapper.map(gameDto, Game.class)).thenThrow(new RuntimeException("Mapping failed"));

        assertThrows(RuntimeException.class, () -> gameService.save(gameDto));
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        GameDto gameDto = new GameDto();
        Game game = new Game();

        when(gameDao.findById(id)).thenReturn(Optional.of(game));
        when(gameDao.save(game)).thenReturn(game);
        when(modelMapper.map(game, GameDto.class)).thenReturn(gameDto);

        GameDto result = gameService.update(id, gameDto);

        assertNotNull(result);
        assertEquals(gameDto, result);
    }

    @Test
    void testUpdate_NotFound() {
        UUID id = UUID.randomUUID();
        GameDto gameDto = new GameDto();

        when(gameDao.findById(id)).thenReturn(Optional.empty());

        GameDto result = gameService.update(id, gameDto);

        assertNull(result);
    }


    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Game game = new Game();

        when(gameDao.findById(id)).thenReturn(Optional.of(game));
        doNothing().when(wishlistService).deleteByGameId(id);
        doNothing().when(reviewService).deleteByGameId(id);
        doNothing().when(userService).deleteGameFromUsers(id);
        doNothing().when(gameDao).deleteById(id);

        boolean result = gameService.delete(id);

        assertTrue(result);
    }

    @Test
    void testDelete_NotFound() {
        UUID id = UUID.randomUUID();

        when(gameDao.findById(id)).thenReturn(Optional.empty());

        boolean result = gameService.delete(id);

        assertFalse(result);
    }
}