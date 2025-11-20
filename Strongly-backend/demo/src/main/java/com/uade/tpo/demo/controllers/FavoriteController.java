package com.uade.tpo.demo.controllers;

import com.uade.tpo.demo.exceptions.AlreadyFavoriteException;
import com.uade.tpo.demo.exceptions.NotFoundException;
import com.uade.tpo.demo.service.FavoriteService;
import java.util.List;
import com.uade.tpo.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.uade.tpo.demo.entity.dto.FavoriteDto;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication !=null && authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        throw new SecurityException("Usuario no encontrado");
        
    }
    
   
    @GetMapping
    public ResponseEntity<List<FavoriteDto>> getFavorites() {
        Long userId = getCurrentUserId(); //Doble funcionalidad
        List<FavoriteDto> products = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(products);
    }



    @PostMapping("/{productId}")
    public ResponseEntity<String> addFavorite(@PathVariable Long productId) {
        try {
            Long userId = getCurrentUserId(); 
            favoriteService.addFavorite(userId, productId);
            return new ResponseEntity<>("Producto añadido a favoritos.", HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AlreadyFavoriteException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK); 
        }
    }

 
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long productId) {
        try {
            Long userId = getCurrentUserId(); 
            favoriteService.removeFavorite(userId, productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el producto de la lista de favoritos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}