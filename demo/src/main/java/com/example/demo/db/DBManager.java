package com.example.demo.db;

import java.util.ArrayList;

public class DBManager {
    private static ArrayList<ShopItem> shopItems = new ArrayList<>();


    static {
        shopItems.add(new ShopItem(1L, "Iphone 12 Pro", "5G goes Pro. A14 Bionic rockets past every other smartphone chip. The Pro camera system takes low-light photography to the next level — with an even bigger jump on iPhone 12 Pro Max. And Ceramic Shield delivers four times better drop performance.",
                990, 8, 5, "https://pbs.twimg.com/media/EFlfPlyXYAUMp2u.jpg"));
        shopItems.add(new ShopItem(2L, "Meizu 16", "Meizu 16th comes with a screen size of 6.0 inches Super AMOLED, FHD+ Display resolution of 1080 x 2160 pixels, 19:9 Aspect Ratio.",
                550, 12, 3, "https://img.gkbcdn.com/s3/p/2018-08-13/meizu-16th-plus-6-5-inch-8gb-128gb-smartphone-black-1571994245281.jpg"));
        shopItems.add(new ShopItem(3L, "Samsung s20", "Meet Galaxy S20, S20+ and S20 Ultra 5G. With revolutionary 8K Video Snap, 5G connectivity and Space Zoom up to 100x, the way you capture and share your life will never be the same.1, 2 Shots you never even knew were there are suddenly just a tap away.",
                800, 10, 4, "https://images-na.ssl-images-amazon.com/images/I/71efuy%2B3ZNL._AC_SL1500_.jpg"));
        shopItems.add(new ShopItem(4L, "Nokia 9", "Nokia 9 PureView smartphone was launched on 25th February 2019. The phone comes with a 5.99-inch touchscreen display. Nokia 9 PureView is powered by an octa-core Qualcomm Snapdragon 845 processor.",
                400, 5, 3, "https://static.techspot.com/images/products/2018/smartphones/org/2019-02-25-product.png"));


    }

    private static Long id = 5L;

    public static ArrayList<ShopItem> getAllShopItems(){
        return shopItems;
    }

    public static void addShopItem(ShopItem shopItem){
        shopItem.setId(id);
        shopItems.add(shopItem);
        id++;

    }

    public static ShopItem getShopItem(Long id){
        for(ShopItem ts : shopItems){
            if(ts.getId() == id){
                return ts;
            }
        }
        return null;
    }

    public static void saveShopItem(ShopItem shopItem){
        for(int i=0; i<shopItems.size(); i++){
            if(shopItems.get(i).getId().equals(shopItem.getId())){
                shopItems.set(i, shopItem);
                break;
            }
        }
    }

    public static void deleteShopItem(Long id){
        for(int i=0; i<shopItems.size(); i++){
            if(shopItems.get(i).getId().equals(id)){
                shopItems.remove(i);
                break;
            }
        }
    }
}
