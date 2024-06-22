//package unical.enterprise.jokibackend.Data.Services;
//
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import unical.enterprise.jokibackend.Data.Dao.AdminDao;
//import unical.enterprise.jokibackend.Data.Entities.Admin;
//import unical.enterprise.jokibackend.Data.Services.Interfaces.AdminService;
//import unical.enterprise.jokibackend.Data.Dto.AdminDto;
//import unical.enterprise.jokibackend.Data.Dto.GameDto;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.UUID;
//
//
//@Service
//@RequiredArgsConstructor
//public class AdminServiceImpl implements AdminService{
//
//    private final AdminDao adminDao;
//    private final ModelMapper modelMapper;
//
//    public void save(Admin admin) {
//        adminDao.save(admin);
//    }
//
//    @Override
//    public AdminDto getById(UUID id) {
//        Admin admin = adminDao.findById(id).orElse(null);
//        return modelMapper.map(admin, AdminDto.class);
//    }
//
//    @Override
//    public AdminDto getByUsername(String username) {
//        Admin admin = adminDao.findAdminByUsername(username).orElse(null);
//        return modelMapper.map(admin, AdminDto.class);
//    }
//
//    @Override
//    public AdminDto getByEmail(String email) {
//        Admin admin = adminDao.findAdminByEmail(email).orElse(null);
//        return modelMapper.map(admin, AdminDto.class);
//    }
//
//    @Override
//    public Collection<AdminDto> findAll() {
//        return adminDao.findAll()
//                .stream().map(admin -> modelMapper
//                .map(admin, AdminDto.class))
//                .toList();
//    }
//
//    // @Override
//    // public Collection<GameDto> getGamesInsertByAdminId(UUID id) {
//    //     List<GameDto> games = adminDao.findGamesInsertByAdminId(id)
//    //             .stream().map(game -> modelMapper
//    //             .map(game, GameDto.class))
//    //             .toList();
//    //     return games;
//    // }
//
//    @Override
//    public AdminDto updateAdmin(UUID id, AdminDto adminDto) {
//        Admin admin = modelMapper.map(adminDto, Admin.class);
//        adminDao.save(admin);
//        return modelMapper.map(admin, AdminDto.class);
//    }
//
//    public AdminDto save(AdminDto adminDto) {
//        Admin admin = modelMapper.map(adminDto, Admin.class);
//        adminDao.save(admin);
//        return modelMapper.map(admin, AdminDto.class);
//    }
//
//    public void delete(UUID id) {
//        adminDao.deleteById(id);
//    }
//}
