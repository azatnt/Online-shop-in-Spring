package com.example.demo.repositories;

import com.example.demo.entities.Items;
import com.example.demo.entities.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Pictures, Long> {

    List<Pictures> findAllByItemId(Long id);

}
