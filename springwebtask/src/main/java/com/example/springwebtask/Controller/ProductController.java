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
        model.addAttribute("categories",pgProductService.categories());
        return "insert";
    }

    @PostMapping("/product-add")
    public String postAdd(@Validated @ModelAttribute("productForm")ProductForm productForm,BindingResult bindingResult,Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories",pgProductService.categories());
            return "insert";
        }

        pgProductService.insert(productForm.getProduct_id(),productForm.getCategory(),productForm.getName(),productForm.getPrice(),productForm.getText());
        return "redirect:/product-add";
    }

    //詳細
    @GetMapping("/date/{id}")
    private String getDetail(@PathVariable("id")String id,Model model){
        model.addAttribute("products",pgProductService.findByProductId(id));
        model.addAttribute("categories",pgProductService.categories());
        return "productDate";
    }

    //削除
    @PostMapping("/delete/{product_id}")
    public String delete(@PathVariable("product_id")String product_id){
        pgProductService.delete(product_id);
        return "redirect:/menu";
    }

    //更新
    @GetMapping("/update/{product_id}")
    private String getUpdate(@PathVariable("product_id")String product_id,@ModelAttribute("updateForm") UpdateForm updateForm,Model model){
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
    private String postUpdate(@PathVariable("product_id")String product_id,@ModelAttribute("updateForm") UpdateForm updateForm,Model model){
       try {
           var record = pgProductService.update(new UpdateRecord(updateForm.getId(),updateForm.getProduct_id(),updateForm.getCategory_id(),updateForm.getName(),updateForm.getPrice(),updateForm.getDescription()));
       }catch (DuplicateKeyException e){
           model.addAttribute("products",pgProductService.updateProductId(updateForm.getKeep()));
           var productList =pgProductService.updateProductId(updateForm.getKeep());
           updateForm.setId(productList.id());
           updateForm.setProduct_id(productList.product_id());
           updateForm.setName(productList.name());
           updateForm.setPrice(productList.price());
           updateForm.setCategory_id(productList.category_id());
           updateForm.setDescription(productList.description());

           updateForm.setKeep(productList.product_id());

           model.addAttribute("categories",pgProductService.categories());

           model.addAttribute("error","ざんねーん");

           return "update";
       }
        return "redirect:/menu";
    }

    //ログアウト
    @PostMapping("/logout")
    public String logout(@ModelAttribute("loginForm") LoginForm loginForm) {
        session.invalidate();
        return "redirect:/login";
    }

}
