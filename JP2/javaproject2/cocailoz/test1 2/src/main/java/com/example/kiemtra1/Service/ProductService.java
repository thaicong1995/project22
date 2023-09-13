package com.example.kiemtra1.Service;

import com.example.kiemtra1.Exception.UserNotFoundException;

import com.example.kiemtra1.Model.CartItem;
import com.example.kiemtra1.Model.Order;
import com.example.kiemtra1.Model.Product;
import com.example.kiemtra1.Model.StatusProduct;
import com.example.kiemtra1.Repo.CartitemRepo;
import com.example.kiemtra1.Repo.ProductRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    private final  Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepo productRepo;
    @Value("${uploading.videoSaveFolder}")
    private  String FOLDER_PATH ;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }
    @Autowired
    public CartitemRepo cartitemRepo;

    public Product addProduct(Product product, MultipartFile file ) {
        product.setCreate_at(LocalDateTime.now());
        product.setUpdate_at(LocalDateTime.now());
        String filePath = FOLDER_PATH +"/" + file.getOriginalFilename();
        product.setImg(file.getOriginalFilename());
        product.setType(file.getContentType());
        product.setPathImg(filePath);
        try {
            file.transferTo(new File(filePath));
            logger.info("save product success" + product.getProductName());
            return productRepo.save(product);
        } catch (IOException e) {
            logger.info("save product success" + product.getProductName());
            throw new RuntimeException(e);
        }
    }
    public List<Product> findAllProduct() {
        logger.info("take List product " );
        return productRepo.findByStatus(StatusProduct.InStock);
    }
    public Product updateProduct(Long id, Product newProduct) {
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
                    logger.info("update Product success " +product.getProductName());
                    return productRepo.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setId(newProduct.id);
                    return productRepo.save(newProduct);
                });
    }
    public Product findProdudtById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public void deleteProduct(Long id) {
        logger.warn("Deleting product with ID: " + id);
        productRepo.deleteProductById(id);
    }
    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProductsByKeyword(keyword);
    }

    public byte[] downloadImageFromFileSystem(String filename) throws IOException {
        Optional<Product> product = productRepo.findByImg(filename);
        String filePath=product.get().getPathImg();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
    public List<Product> getProductsByCategory(String Category) {
        return productRepo.findByCategory(Category);
    }

    public List<Product>top4product(){
        return productRepo.findTop4ByOrderByIdDesc();
    }

}


