package com.example.blog_platform_api.common.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @PrePersist
    public void onCreate()
    {
        if(this instanceof Auditable auditable)
        {
            LocalDateTime now=LocalDateTime.now();
            auditable.setCreatedAt(now);
            auditable.setUpdatedAt(now);
        }
    }

    @PreUpdate
    public void onUpdate()
    {
        if(this instanceof  Auditable auditable)
        {
            auditable.setUpdatedAt(LocalDateTime.now());
        }
    }


}