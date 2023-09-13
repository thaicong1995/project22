
package com.example.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@Table(name = "Order_table")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String orderNo  ;

        private String membername;

        private String email;

        private Status status;

        private String address;

        private String phoneNo;

        private LocalDateTime orderDate;

        private double orderAmount;

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "cartItem_id")
        private CartItem cartItems;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "memberAccount_id")
        private MemberAccount Account_Order;


}
