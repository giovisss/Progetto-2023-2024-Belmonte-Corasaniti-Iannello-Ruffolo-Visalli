package unical.enterprise.jokibackend.Data.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import unical.enterprise.jokibackend.Data.Dao.AdminDao;
import unical.enterprise.jokibackend.Data.Dto.AdminDto;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.Admin;
import unical.enterprise.jokibackend.Data.Entities.Game;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminDao adminDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GameDto gameDto;

    @InjectMocks
    private AdminServiceImpl adminService;

    private UUID sampleId;
    private Admin sampleAdmin;
    private AdminDto sampleAdminDto;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleAdmin = new Admin();
        sampleAdmin.setId(sampleId);
        sampleAdminDto = new AdminDto();
        sampleAdminDto.setId(sampleId);

        lenient().when(adminDao.findById(sampleId)).thenReturn(Optional.of(sampleAdmin));
        lenient().when(modelMapper.map(sampleAdminDto, Admin.class)).thenReturn(sampleAdmin);
        lenient().when(modelMapper.map(sampleAdmin, AdminDto.class)).thenReturn(sampleAdminDto);
    }

    @Test
    void testGetById_Found() {
        when(adminDao.findById(sampleId)).thenReturn(Optional.of(sampleAdmin));
        when(modelMapper.map(sampleAdmin, AdminDto.class)).thenReturn(sampleAdminDto);

        AdminDto result = adminService.getById(sampleId);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        verify(adminDao, times(1)).findById(sampleId);
    }

    @Test
    void testGetById_NotFound() {
        when(adminDao.findById(sampleId)).thenReturn(Optional.empty());

        AdminDto result = adminService.getById(sampleId);

        assertNull(result);
        verify(adminDao, times(1)).findById(sampleId);
    }

    @Test
    void testGetByUsername_Found() {
        String username = "admin";
        when(adminDao.findAdminByUsername(username)).thenReturn(Optional.of(sampleAdmin));
        when(modelMapper.map(sampleAdmin, AdminDto.class)).thenReturn(sampleAdminDto);

        AdminDto result = adminService.getByUsername(username);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        verify(adminDao, times(1)).findAdminByUsername(username);
    }

    @Test
    void testGetByUsername_NotFound() {
        String username = "admin";
        when(adminDao.findAdminByUsername(username)).thenReturn(Optional.empty());

        AdminDto result = adminService.getByUsername(username);

        assertNull(result);
        verify(adminDao, times(1)).findAdminByUsername(username);
    }

    @Test
    void testGetByEmail_Found() {
        String email = "admin@example.com";
        when(adminDao.findAdminByEmail(email)).thenReturn(Optional.of(sampleAdmin));
        when(modelMapper.map(sampleAdmin, AdminDto.class)).thenReturn(sampleAdminDto);

        AdminDto result = adminService.getByEmail(email);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        verify(adminDao, times(1)).findAdminByEmail(email);
    }

    @Test
    void testGetByEmail_NotFound() {
        String email = "admin@example.com";
        when(adminDao.findAdminByEmail(email)).thenReturn(Optional.empty());

        AdminDto result = adminService.getByEmail(email);

        assertNull(result);
        verify(adminDao, times(1)).findAdminByEmail(email);
    }

    @Test
    void testUpdateAdmin_Success() {
        sampleAdminDto.setUsername("newUsername"); // Set the username field

        when(modelMapper.map(sampleAdminDto, Admin.class)).thenReturn(sampleAdmin);
        when(modelMapper.map(sampleAdmin, AdminDto.class)).thenReturn(sampleAdminDto);
        when(adminDao.save(sampleAdmin)).thenReturn(sampleAdmin);

        AdminDto result = adminService.updateAdmin(sampleId, sampleAdminDto);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        assertEquals("newUsername", result.getUsername());

        verify(modelMapper, times(1)).map(sampleAdminDto, Admin.class);
        verify(adminDao, times(1)).save(sampleAdmin);
    }

    @Test
    void testUpdateAdmin_NullDto() {
        AdminDto result = adminService.updateAdmin(sampleId, null);

        assertNull(result);

        verify(adminDao, never()).save(any(Admin.class));
    }

    @Test
    void testFindGamesByAdminUsername_Success() {
        String username = "admin";
        List<Game> games = List.of(new Game());
        when(adminDao.findGamesByAdminUsername(username)).thenReturn(Optional.of(games));
        when(modelMapper.map(any(Game.class), eq(GameDto.class))).thenReturn(new GameDto());

        Optional<Collection<GameDto>> result = adminService.findGamesByAdminUsername(username);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(adminDao, times(1)).findGamesByAdminUsername(username);
    }

    @Test
    void testFindGamesByAdminUsername_NotFound() {
        String username = "admin";
        when(adminDao.findGamesByAdminUsername(username)).thenReturn(Optional.empty());

        Optional<Collection<GameDto>> result = adminService.findGamesByAdminUsername(username);

        assertFalse(result.isPresent());
        verify(adminDao, times(1)).findGamesByAdminUsername(username);
    }
}