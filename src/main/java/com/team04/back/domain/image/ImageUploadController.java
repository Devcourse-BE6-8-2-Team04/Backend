package com.team04.back.domain.image;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {

    // 업로드 폴더 경로
    private final String uploadDir = "./uploaded-images";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + ext;
        File dest = new File(uploadDir, filename);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);

        // 이미지 접근 URL 반환 (예시: /api/v1/images/file/...)
        String imageUrl = "/api/v1/images/file/" + filename;
        return ResponseEntity.ok(Map.of("url", imageUrl));
    }

    // 업로드된 이미지를 반환하는 API (간단 예시)
    @GetMapping("/file/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) throws IOException {
        File file = new File(uploadDir, filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        // 실제 서비스에서는 Content-Type 세팅 필요
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(java.nio.file.Files.readAllBytes(file.toPath()));
    }
}