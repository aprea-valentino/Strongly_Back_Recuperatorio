package com.uade.tpo.demo.entity.dto;
import java.math.BigDecimal;
import java.util.Objects;

public class FavoriteDto {
   

    private String name;
    private String description;
    private BigDecimal price;


    public FavoriteDto(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    public FavoriteDto() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteDto that = (FavoriteDto) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price);
    }

    // --- 6. Opcional: toString() para logging y debugging ---
    @Override
    public String toString() {
        return "FavoriteProductDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}