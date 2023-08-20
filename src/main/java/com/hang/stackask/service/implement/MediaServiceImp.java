package com.hang.stackask.service.implement;

import com.hang.stackask.data.MediaData;
import com.hang.stackask.entity.Media;
import com.hang.stackask.exception.MediaNotFoundException;
import com.hang.stackask.exception.NotSavedException;
import com.hang.stackask.repository.MediaRepository;
import com.hang.stackask.service.interfaces.IAnswerService;
import com.hang.stackask.service.interfaces.IMediaService;
import com.hang.stackask.service.interfaces.IQuestionService;
import com.hang.stackask.service.interfaces.IUserService;
import com.hang.stackask.utils.CustomFileNameUtil;
import com.hang.stackask.utils.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MediaServiceImp implements IMediaService {
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IQuestionService iQuestionService;

    @Autowired
    private IAnswerService iAnswerService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${upload.dir}")
    private String dirName;

    @Override
    public MediaData create(Long user_id, String parent_uuid, MultipartFile multipartFile) throws IOException {
        iUserService.getById(user_id);

        if(parent_uuid.charAt(0) == 'Q')
            iQuestionService.getByUuid(parent_uuid);
        else
            iAnswerService.getByUuid(parent_uuid);

        String uuid = UUID.randomUUID().toString();
        String fileName = CustomFileNameUtil.changeFileName(
                StringUtils.cleanPath(multipartFile.getOriginalFilename()), uuid);

        Media media = Media.builder()
                .uuid(uuid)
                .fileSize(multipartFile.getSize())
                .fileName(fileName)
                .fileType(multipartFile.getContentType())
                .parentUuid(parent_uuid)
                .uploadedBy(user_id)
                .createdTime(LocalDateTime.now())
                .build();

        FileUploadUtil.saveFile(dirName, fileName, multipartFile);

        mediaRepository.save(media);

        MediaData mediaData = modelMapper.map(media, MediaData.class);
        return mediaData;
    }

    @Override
    public MediaData delete(String uuid) {
        Media existedMedia = mediaRepository.getMediaByUuidAndAndEnabledIsTrue(uuid);

        if(existedMedia == null)
            throw new MediaNotFoundException("media not found");

        existedMedia.setEnabled(false);
        Media updatedMedia = mediaRepository.save(existedMedia);

        if (updatedMedia == null)
            throw new NotSavedException("media update fail");

        MediaData mediaData = modelMapper.map(updatedMedia, MediaData.class);
        return mediaData;
    }

    @Override
    public MediaData get(String uuid) {
        Media existedMedia = mediaRepository.getMediaByUuidAndAndEnabledIsTrue(uuid);

        if(existedMedia == null)
            throw new MediaNotFoundException("media not found");

        MediaData mediaData = modelMapper.map(existedMedia, MediaData.class);
        return mediaData;
    }
}
