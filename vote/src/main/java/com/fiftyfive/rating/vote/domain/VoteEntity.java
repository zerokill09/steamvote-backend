package com.fiftyfive.rating.vote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name="votes")
@NoArgsConstructor
public class VoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long appId;
    private Long userId;
    @Setter
    private Boolean liked;

    public VoteEntity(Long userId, Long appId, Boolean liked) {
        this.userId = userId;
        this.appId = appId;
        this.liked = liked;
    }
}
