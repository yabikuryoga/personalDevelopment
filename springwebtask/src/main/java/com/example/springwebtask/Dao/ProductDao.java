package com.example.springwebtask.Dao;

import com.example.springwebtask.Entity.*;

import java.util.List;

public interface ProductDao {
    List<UsersRecord> findAll();

    List<ProductRecord>listAll();

    UsersRecord findByLogin_id(String login_id);

    //検索
    List<ProductRecord> findByName(String name);

    //追加
    int insert(String product_id, int category_id, String name, int price, String description);

    //詳細
    ProductDate findByProductId(String product_id);

    //削除
    int delete(String product_id);

    //カテゴリ
    List<categoriesRecord>categories();

    //更新
    int update(UpdateRecord record);

    UpdateRecord updateProductId(String product_id);
}
