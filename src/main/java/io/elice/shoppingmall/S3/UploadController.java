package io.elice.shoppingmall.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadController {

    private final S3Uploader s3Uploader;

    @PostMapping("/upload")
    public List<String> upload(MultipartFile multipartFile) throws IOException {
        return s3Uploader.uploadFiles(multipartFile, "bucket폴더명");
    }
}
