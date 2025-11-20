package com.uade.tpo.demo.entity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import com.uade.tpo.demo.keys.FavoriteId;

@Entity
@Table(name = "favorites")
@IdClass(FavoriteId.class)
public class Favorite implements Serializable {
   

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
    

    public Favorite() {
    }

    public Favorite(User user, Product product) {
        this.user = user;
        this.product = product;
    }

   

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //Como es una clave compuesta
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(user, favorite.user) && Objects.equals(product, favorite.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, product);
    }
}
