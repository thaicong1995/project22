package com.example.admin.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor

@Data
@Entity
@Getter
@Setter

public class Product {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id ;
    public String productName;
    public double price ;
    public String description;
    public String category ;
    public LocalDateTime create_at ;
    public LocalDateTime update_at ;
    @Column(unique = false)
    public String img;
    private String pathImg;
    private String type;
    public String note ;
    @Enumerated(EnumType.STRING)
    public StatusProduct status ;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();

}
