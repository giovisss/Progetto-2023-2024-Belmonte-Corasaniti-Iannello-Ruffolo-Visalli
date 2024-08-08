package unical.enterprise.jokibackend.Data.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.AdminDao;
import unical.enterprise.jokibackend.Data.Entities.Admin;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Services.Interfaces.AdminService;
import unical.enterprise.jokibackend.Data.Dto.AdminDto;
import unical.enterprise.jokibackend.Data.Dto.GameDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;
    private final ModelMapper modelMapper;

    @Override
    public AdminDto getById(UUID id) {
        Admin admin = adminDao.findById(id).orElse(null);
        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public AdminDto getByUsername(String username) {
        Admin admin = adminDao.findAdminByUsername(username).orElse(null);
        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public AdminDto getByEmail(String email) {
        Admin admin = adminDao.findAdminByEmail(email).orElse(null);
        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public Collection<AdminDto> findAll() {
        return adminDao.findAll()
                .stream().map(admin -> modelMapper
                .map(admin, AdminDto.class))
                .toList();
    }

    @Override
    public AdminDto updateAdmin(UUID id, AdminDto adminDto) {
        Admin admin = modelMapper.map(adminDto, Admin.class);
        if (admin == null) {
            return null;
        }
        ModelMapper modelMapperUpdater = new ModelMapper();
            modelMapperUpdater.typeMap(AdminDto.class, Admin.class).addMappings(mapper -> {
                mapper.skip(Admin::setId);
            });
        modelMapperUpdater.map(adminDto, admin);
        adminDao.save(admin);
        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public AdminDto save(AdminDto adminDto) {
        Admin admin = modelMapper.map(adminDto, Admin.class);
        adminDao.save(admin);
        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public void delete(UUID id) {
        adminDao.deleteById(id);
    }

    @Override
    public Optional<Collection<GameDto>> findGamesByAdminUsername(String username) {
        return Optional.ofNullable(adminDao.findGamesByAdminUsername(username)
                .map(games -> games.stream()
                        .map(game -> modelMapper.map(game, GameDto.class))
                        .toList())
                .orElse(null));
    }
}
