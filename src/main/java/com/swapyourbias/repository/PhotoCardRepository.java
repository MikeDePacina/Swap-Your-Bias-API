package com.swapyourbias.repository;

import com.swapyourbias.model.PhotoCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoCardRepository extends JpaRepository<PhotoCard,Long> {
}
