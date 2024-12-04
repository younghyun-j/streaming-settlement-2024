package com.streaming.settlement.streamingsettlement.content.service;

import com.streaming.settlement.streamingsettlement.content.entity.Content;
import com.streaming.settlement.streamingsettlement.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    @Override
    public Content getContent(Long contentId) {
        return findContentById(contentId);
    }

    @Override
    public void isExistContent(Long contentId) {
        findContentById(contentId);
    }

    private Content findContentById(Long contentId) {
        // TODO : Custom Exception 변경
        return contentRepository.findById(contentId)
                .orElseThrow(()-> new IllegalArgumentException("Content not found"));
    }
}
