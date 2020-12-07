package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Basket implements Comparable<Basket>{
    private Items items;
    private int quantity = 0;

    @Override
    public int compareTo(Basket basket) {
        if (basket.getItems().getId().equals(this.items.getId())){
            return 1;
        }
        return 0;
    }
}
