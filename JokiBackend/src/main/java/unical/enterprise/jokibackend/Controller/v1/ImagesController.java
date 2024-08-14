//package unical.enterprise.jokibackend.Controller.v1;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/api/images")
//public class ImagesController {
//
//    @Value("${images.directory:images/}")
//    private String imagesDirectory;
//
//    @GetMapping("/{imageName:.+}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
//        try {
//            Path path = Paths.get(imagesDirectory).resolve(imageName);
//            Resource resource = new ClassPathResource("images/" + imageName);
//            if (!resource.exists()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            String contentType = Files.probeContentType(path);
//            InputStream inputStream = resource.getInputStream();
//            byte[] imageBytes = FileCopyUtils.copyToByteArray(inputStream);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream");
//            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(imageBytes.length));
//
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//
//        } catch (IOException e) {
//            // Gestisci l'errore
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
