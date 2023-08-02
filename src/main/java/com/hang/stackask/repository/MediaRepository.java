package com.hang.stackask.repository;

import com.hang.stackask.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Media getMediaByUuidAndAndEnabledIsTrue(String uuid);
}
