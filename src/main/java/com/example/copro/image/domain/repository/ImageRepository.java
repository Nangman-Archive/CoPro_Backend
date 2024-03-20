package com.example.copro.image.domain.repository;

import com.example.copro.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findImageByConvertImageNameContaining(String fileName);

    List<Image> findAllByIdIn(List<Long> ids);

    boolean existsById(Long id);

    Optional<Image> findImageByConvertImageName(String convertImageName);
}
