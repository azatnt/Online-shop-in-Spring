package com.example.demo.repositories;

import com.example.demo.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByItemsIdOrderByDateAddedDesc(Long id);
}
