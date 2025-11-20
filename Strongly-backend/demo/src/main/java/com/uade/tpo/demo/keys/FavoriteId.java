package com.uade.tpo.demo.keys; 

import java.io.Serializable;
import java.util.Objects;

public class FavoriteId implements Serializable {

   private Long user;
    private Long product;

    public FavoriteId() {
    }

    public FavoriteId(Long user, Long product) {
        this.user = user;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, product);
    }

    
}