package com.example.kiemtra1.Controller;

import com.example.kiemtra1.Model.CartItem;
import com.example.kiemtra1.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartltem")
public class CartitemController {
   // http://localhost:8080/cartltem/add
    public CartItemService cartItemService;

    public CartitemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }
    @PostMapping("/all")
    public ResponseEntity<CartItem>getallcartItem(){
        List<CartItem>cartItems = cartItemService.findAllCartItem();
        return new ResponseEntity(cartItems,HttpStatus.OK);
    }

    @PostMapping("/add2") // co luu tru ca product
    public ResponseEntity<CartItem>savecartItem(@RequestBody CartItem cartItems,
                                                @RequestParam ("product_id") List<Long> product_id){
        CartItem cartItem = cartItemService.save1(cartItems,product_id);
        return new ResponseEntity(cartItem,HttpStatus.OK);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItem) {
        CartItem cartItem1 = cartItemService.updateCartItem(id,cartItem);
        return new ResponseEntity<>(cartItem1, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") Long id) {
        cartItemService.deleteCartItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
