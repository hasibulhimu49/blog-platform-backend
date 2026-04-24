package com.example.blog_platform_api.post.repository;

import com.example.blog_platform_api.common.enums.PostStatus;
import com.example.blog_platform_api.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);

    Page<Post> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, PostStatus status, Pageable pageable);

    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.status = :status AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchByKeywordAndStatus(@Param("keyword") String keyword,
                                        @Param("status") PostStatus status,
                                        Pageable pageable);

    long countByUserId(Long userId);
}