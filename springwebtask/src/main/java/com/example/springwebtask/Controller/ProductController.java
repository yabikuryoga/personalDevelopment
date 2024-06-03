package com.example.springwebtask.Controller;

import com.example.springwebtask.Entity.User;
import com.example.springwebtask.Form.LoginForm;
import com.example.springwebtask.Form.ProductForm;
import com.example.springwebtask.Form.SearchForm;
import com.example.springwebtask.Service.PgProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("login")
    public String getLogin(@ModelAttribute("loginForm")LoginForm loginForm){return "index";}

    @PostMapping("login")
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
    public String getAdd(@ModelAttribute("productForm")ProductForm productForm){
        return "insert";
    }

    @PostMapping("/product-add")
    public String postAdd(@Validated @ModelAttribute("productForm")ProductForm productForm,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "insert";
        }

        pgProductService.insert(productForm.getProduct_id(),productForm.getCategory(),productForm.getName(),productForm.getPrice(),productForm.getText());
        return "redirect:/product-add";
    }

    //詳細
    @GetMapping("/date/{id}")
    private String getUpdate(@PathVariable("id")String id,Model model){
        model.addAttribute("products",pgProductService.findByProductId(id));
        return "productDate";
    }

    //削除
    @PostMapping("/delete/{product_id}")
    public String delete(@PathVariable("product_id")String product_id, Model model){
        model.addAttribute("delete",pgProductService.delete(product_id));
        return "redirect:/menu";
    }


}
