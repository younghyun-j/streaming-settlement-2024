package com.streaming.settlement.streamingsettlement.content.repository;

import com.streaming.settlement.streamingsettlement.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long>, ContentCustomRepository {
}
