package movieland.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadImage(MultipartFile file) throws IOException;

    String deleteImage(String id);
}
