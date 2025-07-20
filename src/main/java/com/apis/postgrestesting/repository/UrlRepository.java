package com.apis.postgrestesting.repository;
import com.apis.postgrestesting.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    List<UrlEntity> findByUserId(Long userId);

}
