package com.example.worklog.repository;

import com.example.worklog.entity.SavedWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<SavedWork, Long> {
}
