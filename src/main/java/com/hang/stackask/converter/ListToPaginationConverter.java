package com.hang.stackask.converter;

import com.hang.stackask.data.QuestionData;
import com.hang.stackask.response.PageResponse;
import com.hang.stackask.response.PageTokenResponse;
import com.hang.stackask.response.QuestionResponse;

import java.util.Base64;
import java.util.List;

import static com.hang.stackask.utils.MapperUtil.mapList;

public class ListToPaginationConverter {
    public static PageResponse<List<QuestionResponse>> toPagination(List<QuestionData> questionsData, int limit){
        int count = questionsData.size() > limit ? limit : questionsData.size();

        List<QuestionData> questionsSelected = questionsData.subList(0, count);
        List<QuestionResponse> questionResponses = mapList(questionsSelected, QuestionResponse.class);

        String next = null;

        if (questionsData.size() > limit)
            next = Base64.getEncoder().encodeToString(questionsSelected.get(count - 1).getId().toString().getBytes());

        PageTokenResponse pageTokenResponse = PageTokenResponse.builder()
                .next(next)
                .build();

        PageResponse<List<QuestionResponse>> pageResponse = PageResponse.<List<QuestionResponse>>builder()
                .items(questionResponses)
                .pageTokenResponse(pageTokenResponse)
                .count(count)
                .build();

        return pageResponse;
    }
}
