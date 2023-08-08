package com.hang.stackask.repository;

import com.hang.stackask.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName (String name);
}
