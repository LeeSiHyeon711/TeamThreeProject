package org.lsh.teamthreeproject.service;

import org.springframework.web.multipart.MultipartFile;

public interface BoardImageService {
    String storeFile(MultipartFile file);

}
