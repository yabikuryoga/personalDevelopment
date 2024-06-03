package com.example.springwebtask.Dao;

import com.example.springwebtask.Entity.ProductDate;
import com.example.springwebtask.Entity.ProductRecord;
import com.example.springwebtask.Entity.UsersRecord;

import java.util.List;

public interface ProductDao {
    List<UsersRecord> findAll();

    List<ProductRecord>listAll();

    UsersRecord findByLogin_id(String login_id);

    //検索
    List<ProductRecord> findByName(String name);

    //追加
    int insert(int product_id, int category_id, String name, int price, String description);

    //詳細
    ProductDate findByProductId(String product_id);

    //削除
    int delete(String product_id);
}
