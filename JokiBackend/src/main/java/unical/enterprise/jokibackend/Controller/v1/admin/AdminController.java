package unical.enterprise.jokibackend.Controller.v1.admin;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.AdminDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.AdminService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getUsersList(){
        Collection<AdminDto> users = adminService.findAll();
        return ResponseEntity.ok(new Gson().toJson(users));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> getUserById(@PathVariable UUID id) {
        AdminDto user = adminService.getById(id);
        if (user != null) {
            return ResponseEntity.ok(new Gson().toJson(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
