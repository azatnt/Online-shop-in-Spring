package com.example.demo.services;

import com.example.demo.entities.*;
import org.aspectj.weaver.ast.Or;

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
    List<Items> getAllItemsByCategoryId(Long id);


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
    List<Items> findAllByBrandIdAndPriceAsc(Long id, int priceFrom, int priceTo);
    List<Items> findAllByBrandIdAndPriceDesc(Long id, int priceFrom, int priceTo);
//    List<Items> findAllByBrandNameAndPriceAsc(String name)


    List<Categories> getAllCategories();
    Categories getCategory(Long id);
    Categories saveCategory(Categories category);
    Categories addCategory(Categories category);
    void deleteCategory(Categories category);


    List<Pictures> getAllPictures();
    Pictures addPicture(Pictures pictures);
    Pictures savePicture(Pictures pictures);
    Pictures getPicture(Long id);
    void deletePicture(Pictures pictures);
    List<Pictures> findAllByItemId(Long id);
    List<Comments> findAllByItemsIdOrderByDateAddedDesc(Long id);


    Order addOrder(Order order);
    List<Order> getAllOrders();
    Order getOrder(Long id);
    void deleteOrder(Order order);


    Comments addComment(Comments comments);
    List<Comments> getAllComments();
    Comments getComment(Long id);
    void deleteComment(Comments comment);
    Comments saveComment(Comments comments);

}
