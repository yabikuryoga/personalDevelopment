package com.example.springwebtask.Form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;

@Data
public class ProductForm {
   @NotEmpty(message = "商品IDは必須です")
   private String product_id;

    @NotEmpty(message = "商品名は必須です")
    private String name;

    @Range(min = 1,message = "単価は必須です")
    @NotNull(message = "単価は必須です")
    private Integer price;

    @Range(min = 1,message = "カテゴリは必須です")
    private int category;

    private String text;
}
