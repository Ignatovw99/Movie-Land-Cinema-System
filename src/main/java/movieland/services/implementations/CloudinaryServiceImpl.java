package movieland.services.implementations;

import com.cloudinary.Cloudinary;
import movieland.services.interfaces.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        File fileToUpload = File.createTempFile("temp-file", Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(fileToUpload);

        return cloudinary.uploader()
                .upload(fileToUpload, new HashMap<>())
                .get("url")
                .toString();
    }

    @Override
    public String deleteImage(String id) {
        return null;
    }
}
