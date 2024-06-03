package com.example.springwebtask.Service;

import com.example.springwebtask.Dao.PgProductDao;
import com.example.springwebtask.Entity.ProductDate;
import com.example.springwebtask.Entity.ProductRecord;
import com.example.springwebtask.Entity.UsersRecord;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PgProductService implements ProductService{

    @Autowired private PgProductDao productDao;

    //表示
    @Override
    public List<UsersRecord> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<ProductRecord> listAll(){return productDao.listAll();}

    //ログイン
    @Override
    public UsersRecord findByLogin_id(String login_id){return productDao.findByLogin_id(login_id);}

    //検索
    @Override
    public List<ProductRecord>findByName(String name){return productDao.findByName(name);}

    //追加
    @Override
    public int insert(int product_id,int category_id,String name,int price,String description){
        return productDao.insert(product_id,category_id,name,price,description);
    }

    //詳細
    @Override
    public ProductDate findByProductId(String product_id){return productDao.findByProductId(product_id);}

    //削除
    @Override
    public int delete (String product_id){return productDao.delete(product_id);}
}
