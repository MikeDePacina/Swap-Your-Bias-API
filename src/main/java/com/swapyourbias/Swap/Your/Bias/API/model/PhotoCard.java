package com.swapyourbias.Swap.Your.Bias.API.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "photocards")
public class PhotoCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String imgPath;

    @Column(nullable = false)
    private String artist;

    @Column(name = "kpop_group")
    private String group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User owner;

    private LocalDateTime datePosted;
    private LocalDateTime dateUpdated;

    @PrePersist
    protected void onCreate() {
        datePosted = LocalDateTime.now();

    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }
}
