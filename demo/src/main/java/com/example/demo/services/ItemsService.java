package com.example.demo.services;

import com.example.demo.entities.Brands;
import com.example.demo.entities.Countries;
import com.example.demo.entities.Items;

import java.util.List;

public interface ItemsService {
    Items addItem(Items item);
    List<Items> getAllItems();
    Items getItem(Long id);
    void deleteItem(Items item);
    Items saveItem(Items item);
    List<Items> getOnTop();
    List<Items> searchName(String name);
    List<Items> findAllByNameLikeOrderByPriceAsc(String name);
    List<Items> findAllByNameLikeOrderByPriceDesc(String name);
    List<Items> filterPriceAsc(String name, int priceFrom, int priceTo);
    List<Items> filterPriceDesc(String name, int priceFrom, int priceTo);

    List<Brands> getAllBrands();
    Brands addBrand(Brands brand);
    Brands saveBrand(Brands brand);
    Brands getBrand(Long id);
    void deleteBrand(Brands brands);

    List<Countries> getAllCountries();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);
    void deleteCountry(Countries country);

    List<Items> findAllByBrandId(Long id);

}
