package com.example.demo.repositories;

import com.example.demo.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Transactional
public interface CategoryRepository extends JpaRepository<Categories, Long> {

}
