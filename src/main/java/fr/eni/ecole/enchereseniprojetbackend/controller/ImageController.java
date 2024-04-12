package fr.eni.ecole.enchereseniprojetbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/upload")
public class ImageController {

    @Value("${upload.directory}")
    private String uploadDirectory;

    @PostMapping()
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Veuillez sélectionner un fichier 2");
        }
        try {
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            return ResponseEntity.ok("Image téléchargée avec succès");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erreur lors du téléchargement de l'image");
        }
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageName) {
        Path imagePath = Paths.get(uploadDirectory, imageName);

        try {
            // Vérifiez si le fichier existe
            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            // Créez une ressource à partir du fichier
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(imagePath));

            // Déterminez le type MIME de l'image
            String contentType = URLConnection.guessContentTypeFromName(imageName);

            // Retournez la réponse avec le contenu de l'image et le type MIME correct
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (IOException e) {
            // Si une erreur survient, renvoyez une réponse d'erreur interne du serveur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> getListImage() {
        Path imagePath = Paths.get(uploadDirectory);

        try {

            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }
            List<String> imageNames = Files.list(imagePath)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(imageNames);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
