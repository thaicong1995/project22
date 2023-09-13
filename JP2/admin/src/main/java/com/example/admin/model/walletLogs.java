package com.example.admin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class walletLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private double Amount;
    private LocalDateTime Date;
    private String walletName ;
    private boolean status = false;
    private double amount_deducted;
    private Long Order_id;



}
