package org.lsh.teamthreeproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class BoardImageServiceImpl implements BoardImageService {

    private final String UPLOAD_DIR = "C:/src/SpringBoot/teamThreeProject/uploads"; // 파일이 저장될 경로

    @Override
    public String storeFile(MultipartFile file) {
        // 파일이 비었는지 확인
        if (file.isEmpty()) {
            throw new RuntimeException("빈 파일은 저장할 수 없습니다.");
        }

        try {
            // 파일명을 고유하게 만들기 위해 UUID 사용
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String storedFileName = UUID.randomUUID().toString() + fileExtension;

            // 파일 저장 경로 설정
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path targetLocation = path.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation);

            // 저장된 파일의 URL 반환 (여기서는 단순 경로로 반환)
            return "/uploads/" + storedFileName;

        } catch (IOException ex) {
            throw new RuntimeException("파일 저장에 실패했습니다. 파일명: " + file.getOriginalFilename(), ex);
        }
    }
}