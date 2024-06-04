package com.example.springwebtask.Service;

import com.example.springwebtask.Entity.*;

import java.util.List;

public interface ProductService {
    List<UsersRecord> findAll();

    List<ProductRecord> listAll();

    UsersRecord findByLogin_id(String login_id);

    List<ProductRecord>findByName(String name);

    //追加
    int insert(String product_id, int category_id, String name, int price, String description);

    //詳細
    ProductDate findByProductId(String product_id);

    //削除
    int delete(String product_id);

    //カテゴリ
    List<categoriesRecord>categories();

    //更新
    int update(UpdateRecord updateRecord);

    UpdateRecord updateProductId(String product_id);
}
