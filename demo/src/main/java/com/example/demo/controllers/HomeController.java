package com.example.demo.controllers;

import com.example.demo.db.DBManager;
import com.example.demo.db.ShopItem;
import com.example.demo.entities.*;
import com.example.demo.repositories.BrandRepository;
import com.example.demo.services.ItemsService;
import com.example.demo.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("currentUser", getUserData());

        List<Items> item = itemsService.getAllItems();
        model.addAttribute("item", item);

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);


//        System.out.println(item);
        return "index";
    }



    @GetMapping(value = "/topItem")
    public String topItems(Model model) {

        model.addAttribute("currentUser", getUserData());

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
                          @RequestParam(name = "brand_id") Long id,
                          @RequestParam(name = "cat") List<Long> catId)

    {

        boolean InTop = false;

        if (inTop.equals("Yes")) {
            InTop = true;
        }


        long millis=System.currentTimeMillis();
        java.sql.Date date_added=new java.sql.Date(millis);


        Brands bnd = itemsService.getBrand(id);

        if(bnd!=null) {

            ArrayList<Categories> categories = new ArrayList<>();
            for(Long cat: catId){
                categories.add(itemsService.getCategory(cat));
            }
            itemsService.addItem(new Items(null, name, description, price,
                    amount, rating, small_picture,
                    large_picture, date_added, InTop, bnd, categories));
        }
        return "redirect:/allItems";
    }


//    @PostMapping(value = "/saveTask")
//    public String saveTask(@RequestParam(name = "id") Long id,
//                          @RequestParam(name = "name") String name,
//                          @RequestParam(name = "description") String description,
//                          @RequestParam(name = "price") int price,
//                          @RequestParam(name = "amount") int amount,
//                          @RequestParam(name = "rating") int rating,
//                           @RequestParam(name = "small_picture") String small_picture,
//                           @RequestParam(name = "large_picture") String large_picture,
//                           @RequestParam(name = "inTop") String inTop,
//                           @RequestParam(name = "brand_id") Long brandId)
//    {
//        boolean InTop = false;
//
//        if (inTop.equals("Yes")) {
//            InTop = true;
//        }
//
//        Items items = itemsService.getItem(id);
//        if (items!=null){
//
//            Brands brands = itemsService.getBrand(brandId);
//
//            if(brands!=null) {
//            items.setName(name);
//            items.setDescription(description);
//            items.setPrice(price);
//            items.setAmount(amount);
//            items.setStars(rating);
//            items.setSmall_picture_url(small_picture);
//            items.setLarge_picture_url(large_picture);
//            items.setInTop(InTop);
//            items.setBrand(brands);
//            itemsService.saveItem(items);
//            }
//        }
//        return "redirect:/";
//    }



    @PostMapping(value = "/deleteItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteItem(@RequestParam(name = "id") Long id) {
        Items items = itemsService.getItem(id);
        if (items!=null){
            itemsService.deleteItem(items);
        }
        return "redirect:/allItems";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable(name = "id") Long id) {

        model.addAttribute("currentUser", getUserData());

        Items item = itemsService.getItem(id);
        model.addAttribute("item", item);

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);


        List<Categories> categories = itemsService.getAllCategories();
        model.addAttribute("categories", categories);


        return "details";
    }


    @GetMapping(value = "/search")
    public String search(Model model,
                         @RequestParam(name = "search") String search)

    {

        model.addAttribute("currentUser", getUserData());

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

        model.addAttribute("currentUser", getUserData());


        List<Brands> brands = itemsService.getAllBrands();


//        if(order.equals("as") && priceFrom==0 && priceTo==0) {
//            item = itemsService.findAllByNameLikeOrderByPriceAsc(name);
//        }
//        else if(order.equals("des") && priceFrom==0 && priceTo==0){
//            item = itemsService.findAllByNameLikeOrderByPriceDesc(name);
//        }
//        else if(!name.equals("") && order.equals("as") && priceFrom>0 && priceTo>0){
//            item = itemsService.filterPriceAsc(name, priceFrom, priceTo);
//        }
//        else if(!name.equals("") && order.equals("des") && priceFrom>0 && priceTo>0){
//            item = itemsService.filterPriceDesc(name, priceFrom, priceTo);
//        }
        if(order.equals("as") && priceFrom==0 && priceTo==0){
            item = itemsService.findAllByBrandId(id);
        }
        else if(!name.equals("") && order.equals("des") && priceFrom==0 && priceTo==0){
            item = itemsService.findAllByBrandId(id);
        }
        else if(priceFrom>0 && priceTo>0 && order.equals("as")){
            item = itemsService.findAllByBrandIdAndPriceAsc(id, priceFrom, priceTo);
        }
        else if(priceFrom>0 && priceTo>0 && order.equals("des")){
            item = itemsService.findAllByBrandIdAndPriceDesc(id, priceFrom, priceTo);
        }
//        else if(priceFrom>0 && priceTo>0 && order.equals("as")){
//            item = itemsService.filterPriceAsc(name, priceFrom, priceTo);
//        }



        model.addAttribute("brands", brands);



        model.addAttribute("item", item);
        return "result";
    }


    @GetMapping(value = "/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String admin(Model model) {
        model.addAttribute("currentUser", getUserData());

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Countries> countries = itemsService.getAllCountries();
        model.addAttribute("countries", countries);

        return "admin";
    }


    @PostMapping(value = "/addBrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addBrand(@RequestParam(name = "name") String name,
                           @RequestParam(name = "country_id") Long id)
    {
        Countries country = itemsService.getCountry(id);

            itemsService.addBrand(new Brands(null, name, country));

        return "redirect:/admin";
    }


    @GetMapping(value = "/admin_brand_detail/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin_detail(Model model, @PathVariable(name = "id") Long id) {

        model.addAttribute("currentUser", getUserData());

        Brands brands = itemsService.getBrand(id);
        model.addAttribute("brands", brands);

        List<Countries> countries = itemsService.getAllCountries();
        model.addAttribute("countries", countries);

        return "brands_detail";
    }


    @PostMapping(value = "/saveBrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteBrand(@RequestParam(name = "id") Long id) {
        Brands brands = itemsService.getBrand(id);
        if (brands!=null){
            itemsService.deleteBrand(brands);
        }
        return "redirect:/admin";
    }



    @GetMapping(value = "/countries")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminCountry(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Countries> countries = itemsService.getAllCountries();
        model.addAttribute("countries", countries);

        return "countries";
    }


    @PostMapping(value = "/addCountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addBrand(@RequestParam(name = "name") String name,
                           @RequestParam(name = "code") String code)
    {

            itemsService.addCountry(new Countries(null, name, code));

        return "redirect:/countries";
    }


    @GetMapping(value = "/admin_country_detail/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin_country_detail(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getUserData());

        Countries countries = itemsService.getCountry(id);
        model.addAttribute("country", countries);
        return "country_detail";
    }


    @PostMapping(value = "/saveCountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCountry(@RequestParam(name = "id") Long id) {
        Countries countries = itemsService.getCountry(id);
        if (countries!=null){
            itemsService.deleteCountry(countries);
        }
        return "redirect:/countries";
    }

    @PostMapping(value = "/assigncategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignCategory(@RequestParam(name = "id") Long id,
                                 @RequestParam(name = "category_id") Long cat_id)
    {
        Categories categories = itemsService.getCategory(cat_id);
        if(categories!=null){
            Items items = itemsService.getItem(id);
            if(items!=null){
                List<Categories> categories1 = items.getCategories();
                if(categories1==null ){
                    categories1 = new ArrayList<>();
                }
                categories1.add(categories);

                itemsService.saveItem(items);
                return "redirect:/admin_items_detail/"+id;

            }
        }
        return "redirect:/allItems";
    }


    @PostMapping(value = "/removecategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeCategory(@RequestParam(name = "id") Long id,
                                 @RequestParam(name = "category_id") Long cat_id)
    {
        Categories categories = itemsService.getCategory(cat_id);
        if(categories!=null){
            Items items = itemsService.getItem(id);
            if(items!=null){
                List<Categories> categories1 = items.getCategories();
                if(categories1==null ){
                    categories1 = new ArrayList<>();
                }
                categories1.remove(categories);

                itemsService.saveItem(items);
                return "redirect:/admin_items_detail/"+id;

            }
        }
        return "redirect:/allItems";
    }







//    @PostMapping(value = "/deleteCategory")
//    public String deleteCategory(@RequestParam(name = "id") Long id,
//                                 @RequestParam(name = "category_id") Long cat_id)
//    {
//        Categories categories = itemsService.getCategory(cat_id);
//        if(categories!=null){
//            Items items = itemsService.getItem(id);
//            if(items!=null){
//                itemsService.deleteCategory(categories);
//                return "redirect:/detail/"+id;
//
//            }
//        }
//        return "redirect:/";
//    }



    @GetMapping(value = "/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminCategory(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Categories> categories = itemsService.getAllCategories();
        model.addAttribute("categories", categories);

        return "category";
    }


    @PostMapping(value = "/addCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategory(@RequestParam(name = "name") String name,
                           @RequestParam(name = "logo_url") String logo_url)
    {

        itemsService.addCategory(new Categories(null, name, logo_url));

        return "redirect:/categories";
    }



    @GetMapping(value = "/admin_category_detail/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin_category_detail(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getUserData());

        Categories categories = itemsService.getCategory(id);
        model.addAttribute("category", categories);
        return "category_detail";
    }


    @PostMapping(value = "/saveCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveCategory(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "logo_url") String logo_url)
    {



        Categories categories = itemsService.getCategory(id);
        if (categories!=null){
            categories.setName(name);
            categories.setLogo_url(logo_url);
            itemsService.saveCategory(categories);
        }
        return "redirect:/categories";
    }


    @PostMapping(value = "/deleteCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCategory(@RequestParam(name = "id") Long id) {
        Categories categories = itemsService.getCategory(id);
        if (categories!=null){
            itemsService.deleteCategory(categories);
        }
        return "redirect:/categories";
    }



    @GetMapping(value = "/allItems")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String adminItems(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Categories> categories = itemsService.getAllCategories();
        model.addAttribute("categories", categories);

        List<Items> items = itemsService.getAllItems();
        model.addAttribute("items", items);

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);

        return "admin_allItems";
    }


    @GetMapping(value = "/admin_items_detail/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String admin_items_detail(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getUserData());

        Items items = itemsService.getItem(id);
        model.addAttribute("items", items);

        List<Brands> brands = itemsService.getAllBrands();
        model.addAttribute("brands", brands);


        List<Categories> categories = itemsService.getAllCategories();
        model.addAttribute("categories", categories);

        categories.removeIf(x -> items.getCategories().contains(x));


        return "item_detail";
    }



//    @GetMapping("/admin_items_detail/{Id}")
//    public String item_details(Model model, @PathVariable(value = "Id") Long id){
//        Items item = itemsService.getItem(id);
//        if (item != null) {
//            List<Brands> brands = itemsService.getAllBrands();
//            model.addAttribute("brands", brands);
//            model.addAttribute("item", item);
//            List<Categories> categories = itemsService.getAllCategories();
//
//            categories.removeIf(x -> item.getCategories().contains(x));
//
//            model.addAttribute("categories", categories);
//
//            return "details_item";
//        }
//        else {
//            return "redirect:/admin_items";
//        }
//    }



    @PostMapping(value = "/saveItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "id") Long id,
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
        return "redirect:/allItems";
    }



    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser", getUserData());
        return "forbidden";
    }


    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("currentUser", getUserData());
        return "login";
    }


    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser", getUserData());
        return "profile";
    }

    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }


    @GetMapping(value = "/registration")
    public String registration(Model model){
        model.addAttribute("currentUser", getUserData());
        return "registration";
    }


    @PostMapping(value = "/registration")
    public String registration(@RequestParam(name = "user_email") String user_email,
                               @RequestParam(name = "user_password") String user_password,
                               @RequestParam(name = "user_password2") String user_password2,
                               @RequestParam(name = "full_name") String full_name){
        if(user_password.equals(user_password2)){
            Users users = new Users();
            users.setEmail(user_email);
            users.setPassword(passwordEncoder.encode(user_password));
            users.setFullName(full_name);

            Roles roles = userService.getRolesByName("ROLE_USER");
            ArrayList<Roles> roles1 = new ArrayList<>();
            roles1.add(roles);
            users.setRoles(roles1);

            userService.addUser(users);
            return "redirect:/login";
        }else{
            return "redirect:/registration";
        }
    }


    @GetMapping(value = "/allUsers")
    public String allUsers(Model model){

        List<Users> users = userService.getAllUsers();
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("users", users);

        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);
        return "admin_allUsers";
    }

    @PostMapping(value = "/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(@RequestParam(name = "user_email") String email,
                          @RequestParam(name = "user_password") String password,
                          @RequestParam(name = "user_password2") String user_password2,
                          @RequestParam(name = "full_name") String full_name,
                          @RequestParam(name = "role") List<Long> roleId)
    {
        if(password.equals(user_password2)){

            ArrayList<Roles> roles = new ArrayList<>();
            for(Long role: roleId){
                roles.add(userService.getRole(role));
            }

            Users users = new Users();
            users.setEmail(email);
            users.setPassword(passwordEncoder.encode(password));
            users.setFullName(full_name);
            users.setRoles(roles);


            userService.addUser(users);
            return "redirect:/allUsers";
        }else{
            return "redirect:/allUsers";
        }
    }



    @GetMapping(value = "/admin_users_detail/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin_users_detail(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getUserData());

        Users users = userService.getUser(id);
        model.addAttribute("users", users);


        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);

        roles.removeIf(x -> users.getRoles().contains(x));


        return "user_detail";
    }


    @PostMapping(value = "/assignrole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRole(@RequestParam(name = "id") Long id,
                             @RequestParam(name = "role_id") Long role_id)
    {
        Roles roles = userService.getRole(role_id);
        if(roles!=null){
            Users users = userService.getUser(id);
            if(users!=null){
                List<Roles> roles1 = users.getRoles();
                if(roles1==null ){
                    roles1 = new ArrayList<>();
                }
                roles1.add(roles);

                userService.saveUser(users);
                return "redirect:/admin_users_detail/"+id;

            }
        }
        return "redirect:/allUsers";
    }




    @PostMapping(value = "/removerole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeRole(@RequestParam(name = "id") Long id,
                             @RequestParam(name = "role_id") Long role_id)
    {
        Roles roles = userService.getRole(role_id);
        if(roles!=null){
            Users users = userService.getUser(id);
            if(users!=null){
                List<Roles> roles1 = users.getRoles();
                if(roles1==null ){
                    roles1 = new ArrayList<>();
                }
                roles1.remove(roles);

                userService.saveUser(users);
                return "redirect:/admin_users_detail/"+id;

            }
        }
        return "redirect:/allUsers";
    }



    @PostMapping(value = "/saveUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveUser(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "full_name") String full_name)
    {


        Users users = userService.getUser(id);
        if (users!=null){
                users.setEmail(email);
                users.setFullName(full_name);

                userService.saveUser(users);

        }
        return "redirect:/allUsers";
    }


    @PostMapping(value = "/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam(name = "id") Long id) {
        Users users = userService.getUser(id);
        if (users!=null){
            userService.deleteUser(users);
        }
        return "redirect:/allUsers";
    }



    @GetMapping(value = "/allRoles")
    public String allRoles(Model model){

        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("roles", roles);

        return "admin_allRoles";
    }



    @PostMapping(value = "/addRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRole(@RequestParam(name = "name") String name,
                          @RequestParam(name = "description") String description)
    {

        userService.addRole(new Roles(null, name, description));

        return "redirect:/allRoles";
    }



    @GetMapping(value = "/admin_roles_detail/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin_role_detail(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getUserData());

        Roles roles = userService.getRole(id);
        model.addAttribute("roles", roles);
        return "role_detail";
    }


    @PostMapping(value = "/saveRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveRole(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "description") String description)
    {



        Roles roles = userService.getRole(id);
        if (roles!=null){
            roles.setName(name);
            roles.setDescription(description);
            userService.saveRole(roles);
        }
        return "redirect:/allRoles";
    }


    @PostMapping(value = "/deleteRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteRole(@RequestParam(name = "id") Long id) {
        Roles roles = userService.getRole(id);
        if (roles!=null){
            userService.deleteRole(roles);
        }
        return "redirect:/allRoles";
    }

    @PostMapping(value = "/saveProfile")

    public String saveProfile(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "name") String name)
    {



        Users users = userService.getUser(id);
        if (users!=null){
            users.setEmail(email);
            users.setFullName(name);
            userService.saveUser(users);
        }
        return "redirect:/profile?succes_fullname";
    }


    @PostMapping(value = "/changePassword")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(@RequestParam(name = "id") Long id,
                                 @RequestParam(name = "old_password") String old_password,
                                 @RequestParam(name = "new_password") String new_password,
                                 @RequestParam(name = "new_password2") String new_password2)
    {



        Users users = userService.getUser(id);
        if (users!=null){
            if(passwordEncoder.matches(old_password, users.getPassword())){
                if(new_password.equals(new_password2)){
                    users.setPassword(passwordEncoder.encode(new_password));
                    userService.saveUser(users);

                    return "redirect:/profile?succes_password";
                }
                return "redirect:/profile?error_new_password";
            }
            return "redirect:/profile?error_old_password";
        }
        return "redirect:/profile?error";
    }


    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_ava") MultipartFile file){
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") || file.getContentType().equals("image/jpg")) {
            try {
                Users currentUser = getUserData();

                String picName = DigestUtils.sha1Hex("avatar_"+currentUser.getId()+"_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName+".jpg");
                Files.write(path, bytes);

                currentUser.setUserAvatar(picName);
                userService.saveUser(currentUser);

                return "redirect:/profile?success";


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }


    @GetMapping(value = "/viewphoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException {
        String pictureUrl = viewPath + defaultPicture;

        if(url!=null && !url.equals("null")){
            pictureUrl = viewPath + url + ".jpg";
        }

        InputStream in;

        try {
            ClassPathResource resource = new ClassPathResource(pictureUrl);
            in = resource.getInputStream();
        }catch (Exception e){

            ClassPathResource resource = new ClassPathResource(viewPath+defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }
}
