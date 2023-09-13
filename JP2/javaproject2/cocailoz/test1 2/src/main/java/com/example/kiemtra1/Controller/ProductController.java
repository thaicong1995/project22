package com.example.kiemtra1.Controller;

//import com.example.kiemtra1.Model.ImageData;
import com.example.kiemtra1.Model.Order;
import com.example.kiemtra1.Model.Product;
//import com.example.kiemtra1.Service.ImgService;
import com.example.kiemtra1.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.valueOf;

@RestController
@RequestMapping("/product")
public class ProductController {
//    http://localhost:8080/product/all{category} save
    @Autowired
    public ProductService productService;
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.findAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Product product = productService.findProdudtById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product product1 = productService.updateProduct(id,product);
        return new ResponseEntity<>(product1, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/searchProducts") // findby productname use search
    public List<Product> searchProducts( @RequestParam("keyword")  String keyword) {
       return productService.searchProducts(keyword);
    }


    @GetMapping("/fileSystem/{filename}")// upload web
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String filename) throws IOException {
        byte[] imageData=productService.downloadImageFromFileSystem(filename);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(valueOf("image/png"))
                .body(imageData);
    }


    // save product and save img
    @PostMapping(value = {"/save"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
        public Product addProduct(@RequestParam("product") String productJson,
                              @RequestParam("image") MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);
            return productService.addProduct(product, image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @GetMapping("/category") // findby category
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/top4Product")
    public ResponseEntity<?> getTop4Product(){
        List<Product>products = productService.top4product();
        return ResponseEntity.ok(products);
    }
}




