package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "amount")
    private int amount;

    @Column(name = "stars")
    private int stars;

    @Column(name = "small_pic_url", length = 500)
    private String small_picture_url;

    @Column(name = "large_pic_url", length = 500)
    private String large_picture_url;

    @Column(name = "date_added")
    private Date dateAdded;

    @Column(name = "inTop")
    private Boolean inTop;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brands brand;


    @ManyToMany(fetch = FetchType.EAGER)
    private List<Categories> categories;



}
