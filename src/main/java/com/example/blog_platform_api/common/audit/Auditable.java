package com.example.blog_platform_api.common.audit;

import java.time.LocalDateTime;

public interface Auditable {
    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);

    LocalDateTime getUpdatedAt();
    void setUpdatedAt(LocalDateTime updatedAt);


    Long getCreatedBy();
    void setCreatedBy(Long createdBy);

    Long getUpdatedBy();
    void setUpdatedBy(Long updatedBy);

}