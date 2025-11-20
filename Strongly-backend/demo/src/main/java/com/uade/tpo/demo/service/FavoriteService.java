package com.uade.tpo.demo.service;
import com.uade.tpo.demo.entity.Favorite;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.FavoriteRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.exceptions.AlreadyFavoriteException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

    @Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    // Requerimiento 1: Añadir productos a la lista de favoritos
    @Transactional
    public Favorite addFavorite(Long userId, Long productId) {
        // 1. Verificar si ya existe (para evitar duplicados)
        if (favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new AlreadyFavoriteException("El producto ya está en la lista de favoritos.");
        }
        
        User user = userRepository.getById(userId);
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productId));    

        Favorite favorite = new Favorite(user, product);
        return favoriteRepository.save(favorite);
    }

    //Lista de productos favoritos
    public List<Product> getFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(Favorite::getProduct) 
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        // Solo elimina si ambos IDs coinciden (el producto debe pertenecer a ese usuario)
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }
}

    
