package crm.logopedia.data.document.controller;


import crm.logopedia.util.environment.RequestMappings;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping(RequestMappings.DOCUMENTS)
@RequiredArgsConstructor
public class DocumentController {

    private final String uploadDir = "resources/documents";  // Ruta donde se almacenarán los archivos

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("patientName") String patientName, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Por favor selecciona un archivo para subir.");
            return ResponseEntity.ok("Successfully uploaded the file");
        }

        try {

            Path patientDir = Paths.get(uploadDir, patientName);

            // Verificar si la carpeta del paciente ya existe, si no, crearla
            if (!Files.exists(patientDir)) {
                Files.createDirectories(patientDir);
            }
            // Construir la ruta completa del archivo
            Path filePath = Paths.get(patientDir.toString(), file.getOriginalFilename());

            // Guardar el archivo en el sistema de archivos
            file.transferTo(filePath);

            model.addAttribute("message", "Archivo subido con éxito: " + file.getOriginalFilename());
        } catch (IOException e) {
            model.addAttribute("message", "Error al subir el archivo.");
            e.printStackTrace();
        }

        return ResponseEntity.ok("Successfully uploaded the file");
    }



    // Método para descargar el archivo
    // Este método usa ResponseEntity para devolver el archivo como una respuesta HTTP
    // y proporciona el archivo como un recurso Resource
    public ResponseEntity<Resource> downloadFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, fileName);
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
