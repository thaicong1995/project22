package com.example.kiemtra1.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Transection  implements Serializable {
    public String status;
    public String message;
    public String date;
}
