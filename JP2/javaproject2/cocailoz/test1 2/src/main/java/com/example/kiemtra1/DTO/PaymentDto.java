package com.example.kiemtra1.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class PaymentDto implements Serializable {
    public String status;
    public String message;
    public String url;
}
