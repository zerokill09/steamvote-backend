package com.fiftyfive.rating.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    Optional<VoteEntity> findByUserIdAndAppId(Long userId, Long appId);
    List<VoteEntity> findByUserIdAndAppIdInAndLikedIsNotNull(Long userId, List<Long> appIds);
}
