package com.hang.stackask.service.interfaces;

import com.hang.stackask.entity.Question;

public interface IDataSyncService {
    void syncDataToElasticsearch(Question question);
}
