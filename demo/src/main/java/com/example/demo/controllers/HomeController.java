package com.example.demo.controllers;

import com.example.demo.db.DBManager;
import com.example.demo.db.ShopItem;
import com.example.demo.entities.Brands;
import com.example.demo.entities.Countries;
import com.example.demo.entities.Items;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemsService itemsService;



    @GetMapping(value = "/")
    public String index(Model model) {

        List<Items> item = itemsService.getAllItems();
        model.addAttribute("item", item);

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);


//        System.out.println(item);
        return "index";
    }



    @GetMapping(value = "/topItem")
    public String topItems(Model model) {


        List<Items> item = itemsService.getOnTop();
        model.addAttribute("item", item);
//        System.out.println(item);
        return "topItems";
    }


    @PostMapping(value = "/addTask")
    public String addTask(@RequestParam(name = "name") String name,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "amount") int amount,
                          @RequestParam(name = "rating") int rating,
                          @RequestParam(name = "small_picture") String small_picture,
                          @RequestParam(name = "large_picture") String large_picture,
                          @RequestParam(name = "inTop") String inTop,
                          @RequestParam(name = "brand_id") Long id)

    {

        boolean InTop = false;

        if (inTop.equals("Yes")) {
            InTop = true;
        }


        long millis=System.currentTimeMillis();
        java.sql.Date date_added=new java.sql.Date(millis);


        Brands bnd = itemsService.getBrand(id);

        if(bnd!=null) {


            itemsService.addItem(new Items(null, name, description, price,
                    amount, rating, small_picture,
                    large_picture, date_added, InTop, bnd));
        }
        return "redirect:/";
    }


    @PostMapping(value = "/saveTask")
    public String saveTask(@RequestParam(name = "id") Long id,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "amount") int amount,
                          @RequestParam(name = "rating") int rating,
                           @RequestParam(name = "small_picture") String small_picture,
                           @RequestParam(name = "large_picture") String large_picture,
                           @RequestParam(name = "inTop") String inTop,
                           @RequestParam(name = "brand_id") Long brandId)
    {
        boolean InTop = false;

        if (inTop.equals("Yes")) {
            InTop = true;
        }

        Items items = itemsService.getItem(id);
        if (items!=null){

            Brands brands = itemsService.getBrand(brandId);

            if(brands!=null) {
            items.setName(name);
            items.setDescription(description);
            items.setPrice(price);
            items.setAmount(amount);
            items.setStars(rating);
            items.setSmall_picture_url(small_picture);
            items.setLarge_picture_url(large_picture);
            items.setInTop(InTop);
            items.setBrand(brands);
            itemsService.saveItem(items);
            }
        }
        return "redirect:/";
    }

    @PostMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam(name = "id") Long id) {
        Items items = itemsService.getItem(id);
        if (items!=null){
            itemsService.deleteItem(items);
        }
        return "redirect:/";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable(name = "id") Long id) {
        Items item = itemsService.getItem(id);
        model.addAttribute("item", item);

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);

        return "details";
    }


    @GetMapping(value = "/search")
    public String search(Model model,
                         @RequestParam(name = "search") String search)

    {

        List<Items> item = itemsService.searchName(search);
        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);


        model.addAttribute("item", item);
        return "result";
    }


    @GetMapping(value = "/filter")
    public String search(Model model,
                         @RequestParam(name = "search", defaultValue = "0") String name,
                         @RequestParam(name = "cat") String order,
                         @RequestParam(name = "priceFrom", defaultValue = "0") int priceFrom,
                         @RequestParam(name = "priceTo", defaultValue = "0") int priceTo,
                         @RequestParam(name = "brand_id") Long id) {

        List<Items> item = null;



        if(order.equals("as") && priceFrom==0 && priceTo==0){
            item = itemsService.findAllByBrandId(id);
        }
        else if(order.equals("as") && priceFrom==0 && priceTo==0) {
            item = itemsService.findAllByNameLikeOrderByPriceAsc(name);
        }
        else if(order.equals("des") && priceFrom==0 && priceTo==0){
            item = itemsService.findAllByNameLikeOrderByPriceDesc(name);
        }
        else if(order.equals("as") && priceFrom>0 && priceTo>0){
            item = itemsService.filterPriceAsc(name, priceFrom, priceTo);
        }
        else if(order.equals("des") && priceFrom>0 && priceTo>0){
            item = itemsService.filterPriceDesc(name, priceFrom, priceTo);
        }






        model.addAttribute("item", item);
        return "filtered";
    }


    @GetMapping(value = "/admin")
    public String admin(Model model) {

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Countries> countries = itemsService.getAllCountries();
        model.addAttribute("countries", countries);

        return "admin";
    }


    @PostMapping(value = "/addBrand")
    public String addBrand(@RequestParam(name = "name") String name,
                           @RequestParam(name = "country_id") Long id)
    {
        Countries country = itemsService.getCountry(id);

            itemsService.addBrand(new Brands(null, name, country));

        return "redirect:/admin";
    }


    @GetMapping(value = "/admin_brand_detail/{id}")
    public String admin_detail(Model model, @PathVariable(name = "id") Long id) {
        Brands brands = itemsService.getBrand(id);
        model.addAttribute("brands", brands);

        List<Countries> countries = itemsService.getAllCountries();
        model.addAttribute("countries", countries);

        return "brands_detail";
    }


    @PostMapping(value = "/saveBrand")
    public String saveBrand(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "country_id") Long count_id)
    {



        Brands brands = itemsService.getBrand(id);
        if (brands!=null){
            Countries countries = itemsService.getCountry(count_id);
            brands.setName(name);
            brands.setCountry(countries);
            itemsService.saveBrand(brands);
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/deleteBrand")
    public String deleteBrand(@RequestParam(name = "id") Long id) {
        Brands brands = itemsService.getBrand(id);
        if (brands!=null){
            itemsService.deleteBrand(brands);
        }
        return "redirect:/admin";
    }



    @GetMapping(value = "/countries")
    public String adminCountry(Model model) {

        List<Countries> countries = itemsService.getAllCountries();
        model.addAttribute("countries", countries);

        return "countries";
    }


    @PostMapping(value = "/addCountry")
    public String addBrand(@RequestParam(name = "name") String name,
                           @RequestParam(name = "code") String code)
    {

            itemsService.addCountry(new Countries(null, name, code));

        return "redirect:/countries";
    }


    @GetMapping(value = "/admin_country_detail/{id}")
    public String admin_country_detail(Model model, @PathVariable(name = "id") Long id) {
        Countries countries = itemsService.getCountry(id);
        model.addAttribute("country", countries);
        return "country_detail";
    }


    @PostMapping(value = "/saveCountry")
    public String saveCountry(@RequestParam(name = "id") Long id,
                            @RequestParam(name = "name") String name,
                            @RequestParam(name = "code") String code)
    {



        Countries countries = itemsService.getCountry(id);
        if (countries!=null){
            countries.setName(name);
            countries.setCode(code);
            itemsService.saveCountry(countries);
        }
        return "redirect:/countries";
    }


    @PostMapping(value = "/deleteCountry")
    public String deleteCountry(@RequestParam(name = "id") Long id) {
        Countries countries = itemsService.getCountry(id);
        if (countries!=null){
            itemsService.deleteCountry(countries);
        }
        return "redirect:/countries";
    }

}
