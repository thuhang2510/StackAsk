package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.MediaData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IMediaService {
    MediaData create(Long userId, String parentUuid, MultipartFile file) throws IOException;
    MediaData delete(String uuid);
    MediaData get(String uuid);
}
