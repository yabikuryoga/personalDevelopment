package com.example.springwebtask.Dao;

import com.example.springwebtask.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PgProductDao implements ProductDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    //全テーブル
    @Override
    public List<UsersRecord> findAll(){
        return jdbcTemplate.query("SELECT * FROM users  ORDER BY id",
                new DataClassRowMapper<>(UsersRecord.class));
    }

    @Override
    public List<ProductRecord>listAll(){
        return jdbcTemplate.query("SELECT product_id,products.name,price,categories.name AS categoriesname FROM products INNER JOIN categories ON products.category_id = categories.id ORDER BY product_id",
                new DataClassRowMapper<>(ProductRecord.class));
    }

    //ログイン
    @Override
    public UsersRecord findByLogin_id(String login_id){
        var param = new MapSqlParameterSource();
        param.addValue("login_id", login_id);
        var list = jdbcTemplate.query("SELECT * FROM users WHERE login_id = :login_id", param, new DataClassRowMapper<>(UsersRecord.class));
        return list.isEmpty() ? null : list.get(0);
    }

    //検索
    @Override
    public List<ProductRecord> findByName(String name){
        var param = new MapSqlParameterSource();
        param.addValue("name", name);
        var list = jdbcTemplate.query("SELECT product_id,products.name,price,categories.name AS categoriesname FROM products INNER JOIN categories ON products.category_id = categories.id WHERE  products.name LIKE '%' || :name || '%' ORDER BY product_id", param,new DataClassRowMapper<>(ProductRecord.class));
        return list;
    }

    //追加
    @Override
    public int insert(String product_id,int category_id,String name,int price,String description){
        var param = new MapSqlParameterSource();
        param.addValue("product_id", product_id);
        param.addValue("category_id", category_id);
        param.addValue("name", name);
        param.addValue("price",price);
        param.addValue("description",description);
        return jdbcTemplate.update("INSERT INTO products (product_id,category_id,name,price,description) VALUES (:product_id,:category_id,:name,:price,:description)", param);
    }

    //詳細
    @Override
    public ProductDate findByProductId(String product_id){
        var param = new MapSqlParameterSource();
        param.addValue("product_id", '%'+ product_id +'%');
        var list = jdbcTemplate.query("SELECT * FROM products WHERE product_id LIKE :product_id", param, new DataClassRowMapper<>(ProductDate.class));
        return list.isEmpty() ? null : list.get(0);
    }



    //削除
    @Override
    public int delete (String product_id){
        var param = new MapSqlParameterSource();
        param.addValue("product_id",product_id);
        return jdbcTemplate.update("DELETE FROM products WHERE product_id = :product_id",param);
    }

    //カテゴリ
    @Override
    public List<categoriesRecord>categories(){
        return jdbcTemplate.query("SELECT id,name FROM categories",new DataClassRowMapper<>(categoriesRecord.class));
    }

    //更新
    @Override
    public int update(UpdateRecord record){
        var param = new MapSqlParameterSource();
        param.addValue("id",record.id());
        param.addValue("product_id",record.product_id());
        param.addValue("name",record.name());
        param.addValue("category_id",record.category_id());
        param.addValue("price",record.price());
        param.addValue("description",record.description());
        return jdbcTemplate.update("UPDATE products SET product_id = :product_id,category_id = :category_id,name = :name,price = :price,description = :description WHERE id = :id",param);
    }

    @Override
    public UpdateRecord updateProductId(String product_id){
        var param = new MapSqlParameterSource();
        param.addValue("product_id", '%'+ product_id +'%');
        var list = jdbcTemplate.query("SELECT * FROM products WHERE product_id LIKE :product_id", param, new DataClassRowMapper<>(UpdateRecord.class));
        return list.isEmpty() ? null : list.get(0);
    }


}
