package com.ecom.admin.admin.controllers;

import com.ecom.library.library.dto.ProductDto;
import com.ecom.library.library.models.Category;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.service.CategorySercive;
import com.ecom.library.library.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.security.Principal;
import java.util.List;

@Controller
public class  ProductController {

    private CategorySercive categorySercive;
    private ProductService productService;

    public ProductController(ProductService productService, CategorySercive categorySercive) {
        this.productService = productService;
        this.categorySercive = categorySercive;
    }

    @GetMapping("/products")
    public String products(Model model, Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        int pageNo = 0;
        Page<Product> products = productService.pageProducts(pageNo);
        System.out.println(products.getSize());
        model.addAttribute("title","Product page");
        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("products",products);
        return "products";
    }


    @GetMapping("/products/{pageNo}")
    public String productPage(@PathVariable("pageNo") int pageNo,Model model,Principal principal){
        if(principal==null){
            return "redirect:/login";
        }

        Page<Product> products = productService.pageProducts(pageNo);
        System.out.println(products.getSize());
        model.addAttribute("title","Product page");
        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("products",products);

        return "products";
    }

    @GetMapping("/search-product/{pageNo}")
    public String productSearch(@PathVariable("pageNo") int pageNo,@RequestParam("keyword") String keyword
            ,Model model,Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        Page<Product>products = productService.searchProducts(keyword,pageNo);
        model.addAttribute("products",products);
        model.addAttribute("size",products.getSize());
        model.addAttribute("currentPage" , pageNo);
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("title", "Products");

        return "result-products";
    }



    @GetMapping("/add-product")
    public String addProductForm(Model model,Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        List<Category> categories = categorySercive.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("product" , new ProductDto());
        model.addAttribute("categoryNew",new Category());
        return "add-products";
    }
    @PostMapping("/save-product")
    public String saveProduct(@Valid @ModelAttribute("product")ProductDto productDto, BindingResult result,
                              @RequestParam("imageProduct")List<MultipartFile> imageProduct,
                              RedirectAttributes attributes, Model model){

        try{
            if (result.hasErrors()){
                for (FieldError error : result.getFieldErrors()) {
                    model.addAttribute("error", error.getDefaultMessage());
                }
//                model.addAttribute("error","Fill the form properly");
                List<Category> categories = categorySercive.findAllByActivated();
                model.addAttribute("categories", categories);
                model.addAttribute("product" , new ProductDto());
                model.addAttribute("categoryNew",new Category());
                return "add-products";

            }
            productService.save(imageProduct,productDto);
            attributes.addFlashAttribute("success","Added Successfylly!!");
        }
        catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            attributes.addFlashAttribute("error","Duplicate name ");

        }
        catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed!!!");
        }

        return "redirect:/products/0";
    }

    @GetMapping("/update-products/{id}")
    public String updateProduct(@PathVariable("id") Long id, Model model, Principal principal) {
        if(principal==null){
            return "redirect:/login";
        }
        List<Category> categories=categorySercive.findAllByActivated();
        ProductDto productDto=productService.getById(id);
        model.addAttribute("title","Update Products");
        model.addAttribute("categories",categories);
        model.addAttribute("productsDto",productDto);

        return "update-products";
    }



    @PostMapping("/update-product/{id}")
    @Transactional
    public String updateProduct(@Valid @ModelAttribute("productDto") ProductDto productDto,BindingResult result,
                                @RequestParam("imageProduct") List<MultipartFile> imageProduct,
                                RedirectAttributes redirectAttributes,
                                @PathVariable("id") Long id) {
        try {
//            System.out.println(id);
            if (result.hasErrors()){
                return "redirect:/update-products/"+id;
            }
            productService.update(imageProduct, productDto,id);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteByid(@PathVariable("id")Long id){
       try {
           productService.softDeleteById(id);
       }catch (Exception e){
           e.printStackTrace();
       }
        return "redirect:/products/0";
    }
    @Transactional
    @GetMapping("/enable-product/{id}")
    public String enableDisable(@PathVariable("id")Long id){
        try {
            productService.enableProduct(id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/products/0";

    }
}
