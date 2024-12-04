package com.streaming.settlement.streamingsettlement.content.service;

import com.streaming.settlement.streamingsettlement.content.entity.Content;

public interface ContentService {
    Content getContent(Long contentId);
    void isExistContent(Long contentId);
}
