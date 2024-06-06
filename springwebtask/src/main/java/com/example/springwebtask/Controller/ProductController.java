package com.example.springwebtask.Controller;

import com.example.springwebtask.Entity.UpdateRecord;
import com.example.springwebtask.Entity.User;
import com.example.springwebtask.Form.LoginForm;
import com.example.springwebtask.Form.ProductForm;
import com.example.springwebtask.Form.SearchForm;
import com.example.springwebtask.Form.UpdateForm;
import com.example.springwebtask.Service.PgProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @Autowired
    PgProductService pgProductService;

    @Autowired
    private HttpSession session;

    //ログイン
    @GetMapping("/login")
    public String getLogin(@ModelAttribute("loginForm")LoginForm loginForm){return "index";}

    @PostMapping("/login")
    public String postLogin(@Validated @ModelAttribute("loginForm")LoginForm loginForm, BindingResult bindingResult,Model model){
        if (bindingResult.hasErrors()) {
            return "index";
        }
        var user = pgProductService.findByLogin_id(loginForm.getLoginId());

        if(user == null){
            model.addAttribute("error","IDまたはパスワードが不正です");
            return "index";
        }

        if (loginForm.getPass().equals(user.password())){
            var sessionUser = new User(user.id(),user.login_id(),user.password(),user.name());
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/menu";
        }
        model.addAttribute("error","IDまたはパスワードが不正です");
        return "index";
    }

    //検索
    @GetMapping("/menu")
    public String getSearch(Model model){
        var session = (User)this.session.getAttribute("sessionUser");
        if (session == null){
            return "redirect:/login";
        }

        model.addAttribute("search",pgProductService.listAll());
        return "menu";
    }


    @PostMapping("/menu")
    public String postSearch(@RequestParam(name="keyword")String name, Model model){
        model.addAttribute("search",pgProductService.findByName(name));
        return "menu";
    }

    //新規登録
    @GetMapping("/product-add")
    public String getAdd(@ModelAttribute("productForm")ProductForm productForm,Model model){
        var session = (User)this.session.getAttribute("sessionUser");
        if (session == null){
            return "redirect:/login";
        }

        model.addAttribute("categories",pgProductService.categories());
        return "insert";
    }

    @PostMapping("/product-add")
    public String postAdd(@Validated @ModelAttribute("productForm")ProductForm productForm,BindingResult bindingResult,Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories",pgProductService.categories());
            return "insert";
        }

        try {
            pgProductService.insert(productForm.getProduct_id(),productForm.getCategory(),productForm.getName(),productForm.getPrice(),productForm.getText());
        }catch (DuplicateKeyException e){
            model.addAttribute("error","商品コードが重複しています");
            return "insert";
        }
        model.addAttribute("completion","登録に成功しました");
        return "success";
    }

    //詳細
    @GetMapping("/date/{id}")
    private String getDetail(@PathVariable("id")String id,Model model){
        var session = (User)this.session.getAttribute("sessionUser");
        if (session == null){
            return "redirect:/login";
        }

        var product = pgProductService.updateProductId(id);
        model.addAttribute("products",product);
        var categories = pgProductService.categories();
        var categoryName = "";
        for(var category : categories ){
            if(category.id() == product.category_id()){
                categoryName = category.name();
            }
        }
        model.addAttribute("categories",categoryName);
        return "detail";
    }

    //削除
    @PostMapping("/delete/{product_id}")
    public String delete(@PathVariable("product_id")String product_id,Model model){
        try {
            pgProductService.delete(product_id);
        }catch (DataAccessException e){
            model.addAttribute("error","削除に失敗しました");
            return "detail";
        }
        model.addAttribute("completion","削除に成功しました");
        return "success";
    }

    //更新
    @GetMapping("/update/{product_id}")
    private String getUpdate(@PathVariable("product_id")String product_id,@ModelAttribute("updateForm") UpdateForm updateForm,Model model){
        var session = (User)this.session.getAttribute("sessionUser");
        if (session == null){
            return "redirect:/login";
        }

        model.addAttribute("products",pgProductService.updateProductId(product_id));
        var productList =pgProductService.updateProductId(product_id);
        updateForm.setId(productList.id());
        updateForm.setProduct_id(productList.product_id());
        updateForm.setName(productList.name());
        updateForm.setPrice(productList.price());
        updateForm.setCategory_id(productList.category_id());
        updateForm.setDescription(productList.description());

        updateForm.setKeep(productList.product_id());

        model.addAttribute("categories",pgProductService.categories());
        return "update";
    }

    @PostMapping("/update/{product_id}")
    private String postUpdate(@Validated @ModelAttribute("updateForm") UpdateForm updateForm ,BindingResult bindingResult,@PathVariable("product_id")String product_id,Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("categories",pgProductService.categories());
            return "update";
        }

        try {
           var record = pgProductService.update(new UpdateRecord(updateForm.getId(),updateForm.getProduct_id(),updateForm.getCategory_id(),updateForm.getName(),updateForm.getPrice(),updateForm.getDescription()));
       }catch (DuplicateKeyException e){
//           model.addAttribute("products",pgProductService.updateProductId(updateForm.getKeep()));
           var productList =pgProductService.updateProductId(updateForm.getKeep());
           updateForm.setId(productList.id());
           updateForm.setProduct_id(productList.product_id());
           updateForm.setName(productList.name());
           updateForm.setPrice(productList.price());
           updateForm.setCategory_id(productList.category_id());
           updateForm.setDescription(productList.description());

           updateForm.setKeep(productList.product_id());

           model.addAttribute("categories",pgProductService.categories());

           model.addAttribute("error","商品コードが重複しています");

           return "update";
       }
        model.addAttribute("completion","更新に成功しました");
        return "success";
    }

    //ログアウト
    @PostMapping("/logout")
    public String logout(@ModelAttribute("loginForm") LoginForm loginForm) {
        session.invalidate();
        return "logout";
    }


}
