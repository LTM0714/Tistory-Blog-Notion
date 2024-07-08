package com.example.myfirstproject.repository;

import com.example.myfirstproject.entity.Notice;
import org.springframework.data.repository.CrudRepository;

public interface NoticeRepository extends CrudRepository<Notice, Long> {
}
