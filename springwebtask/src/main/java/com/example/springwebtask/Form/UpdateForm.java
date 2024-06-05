package com.example.springwebtask.Form;

import lombok.Data;

@Data
public class UpdateForm {
    private int id;
    private String product_id;
    private String name;
    private int price;
    private int category_id;
    private String description;

    private String keep;
}
