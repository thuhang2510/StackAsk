package com.hang.stackask.service.implement;

import com.hang.stackask.entity.Tag;
import com.hang.stackask.repository.TagRepository;
import com.hang.stackask.service.interfaces.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImp implements ITagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag getOrCreatTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName);

        if (tag == null) {
            tag = new Tag();
            tag.setName(tagName);
        }

        return tag;
    }
}
