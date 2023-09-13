package com.example.admin.service;

import com.example.admin.model.Order;
import com.example.admin.model.Product;
import com.example.admin.repository.OrderRepo;
import com.example.admin.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Value("${uploading.videoSaveFolder}")
    private  String FOLDER_PATH ;
    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product addProduct(Product product, MultipartFile file ) {
        product.setCreate_at(LocalDateTime.now());
        product.setUpdate_at(LocalDateTime.now());
        String filePath = FOLDER_PATH +"/" + file.getOriginalFilename();
        product.setImg(file.getOriginalFilename());
        product.setType(file.getContentType());
        product.setPathImg(filePath);
        try {
            file.transferTo(new File(filePath));
            return productRepo.save(product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Product> getAllProduct() {
        return productRepo.findAll(); // Use the correct Product class from the model package
    }

    public byte[] downloadImageFromFileSystem(int productId) throws IOException {
        Optional<Product> productOptional = productRepo.findById((int) productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String filePath = FOLDER_PATH + File.separator + product.getPathImg();
            byte[] images = Files.readAllBytes(new File(filePath).toPath());
            return images;
        }
        return new byte[0];
    }

    public Product getProductById(int productId) {
        return productRepo.findById(productId).orElse(null);
    }

    public Product updateProduct(int id, Product newProduct) {
        return productRepo.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setPrice(newProduct.getPrice());
                    product.setCategory(newProduct.getCategory());
                    if (newProduct.getImg() != null) {
                        product.setImg(newProduct.getImg());
                    }
                    if (newProduct.getPathImg() != null) {
                        product.setPathImg(newProduct.getPathImg());
                    }
                    product.setStatus(newProduct.getStatus());
                    product.setUpdate_at(LocalDateTime.now());
                    return productRepo.save(product);
                })
                .orElse(null);
    }


    public byte[] downloadImageFromFileSystem(String filename) throws IOException {
        Optional<Product> productOptional = productRepo.findByImg(filename);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String filePath = product.getPathImg();
            byte[] images = Files.readAllBytes(new File(filePath).toPath());
            return images;
        } else {
            throw new IllegalArgumentException("Product with filename " + filename + " not found");
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepo.deleteProductById(id);
    }

}




