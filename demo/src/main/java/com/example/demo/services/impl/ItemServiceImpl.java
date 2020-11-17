package com.example.demo.services.impl;

import com.example.demo.entities.Brands;
import com.example.demo.entities.Countries;
import com.example.demo.entities.Items;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.repositories.CountryRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemsService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Items addItem(Items item) {
        return itemsRepository.save(item);
    }

    @Override
    public List<Items> getAllItems() {
        return itemsRepository.findAll();
    }

    @Override
    public Items getItem(Long id) {
        return itemsRepository.getOne(id);
    }

    @Override
    public void deleteItem(Items item) {
        itemsRepository.delete(item);
    }

    @Override
    public Items saveItem(Items item) {
        return itemsRepository.save(item);
    }

    @Override
    public List<Items> getOnTop() {
        return itemsRepository.getAllByInTopTrue();
    }

    @Override
    public List<Items> searchName(String name) {
        name = "%" + name + "%";
        return itemsRepository.findAllByNameLike(name);
    }

    @Override
    public List<Items> findAllByNameLikeOrderByPriceAsc(String name) {
        name = "%" + name + "%";
        return itemsRepository.findAllByNameLikeOrderByPriceAsc(name);
    }

    @Override
    public List<Items> findAllByNameLikeOrderByPriceDesc(String name) {
        name = "%" + name + "%";
        return itemsRepository.findAllByNameLikeOrderByPriceDesc(name);
    }


    @Override
    public List<Items> filterPriceDesc(String name, int priceFrom, int priceTo) {
        name = "%" + name + "%";
        return itemsRepository.findAllByNameLikeAndPriceBetweenOrderByPriceDesc(name, priceFrom, priceTo);
    }


    @Override
    public List<Items> filterPriceAsc(String name, int priceFrom, int priceTo) {
        name = "%" + name + "%";
        return itemsRepository.findAllByNameLikeAndPriceBetweenOrderByPriceAsc(name, priceFrom, priceTo);
    }


    @Override
    public List<Brands> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brands addBrand(Brands brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brands saveBrand(Brands brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brands getBrand(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public void deleteBrand(Brands brands) {
        brandRepository.delete(brands);
    }

    @Override
    public List<Countries> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Countries addCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries getCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public void deleteCountry(Countries country) {
        countryRepository.delete(country);
    }

    @Override
    public List<Items> findAllByBrandId(Long id) {
        return itemsRepository.findAllByBrandId(id);
    }


}
