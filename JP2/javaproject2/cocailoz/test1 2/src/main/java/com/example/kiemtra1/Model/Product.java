package com.example.kiemtra1.Model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Getter
@Setter
@Builder
public class Product {
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
