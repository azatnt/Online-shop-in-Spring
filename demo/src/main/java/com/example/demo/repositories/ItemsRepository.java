package com.example.demo.repositories;

import com.example.demo.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ItemsRepository extends JpaRepository<Items, Long> {
    List<Items> getAllByInTopTrue();

    @Query("SELECT it FROM Items it WHERE LOWER(it.name) LIKE LOWER(?1)")
    List<Items> findAllByNameLike(String name);
    List<Items> findAllByNameLikeOrderByPriceAsc(String name);
    List<Items> findAllByNameLikeOrderByPriceDesc(String name);
    List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, int priceFrom, int priceTo);
    List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, int priceFrom, int priceTo);
    List<Items> findAllByBrandId(Long id);
    List<Items> findAllByBrandIdAndPriceBetweenOrderByPriceAsc(Long id, int priceFrom, int priceTo);
    List<Items> findAllByBrandIdAndPriceBetweenOrderByPriceDesc(Long id, int priceFrom, int priceTo);


}
