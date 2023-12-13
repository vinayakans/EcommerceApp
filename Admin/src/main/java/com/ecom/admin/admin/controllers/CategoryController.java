package com.ecom.admin.admin.controllers;

import com.ecom.library.library.models.Category;
import com.ecom.library.library.service.CategorySercive;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class CategoryController {
    private CategorySercive categorySercive;

    public CategoryController(CategorySercive categorySercive) {
        this.categorySercive = categorySercive;
    }


    @GetMapping("/categories")
    public String categories(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        int pageNo =0;
        Page<Category> categories = categorySercive.pageCategory(pageNo);
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Categories");
        model.addAttribute("size", categories.getSize());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categoryDto", new Category());
        return "categories";
    }

    @PostMapping("/add-category")
    public String addCategory(@Valid Category category,
                              RedirectAttributes attributes){

        try {
            System.out.println(category.getName());
            categorySercive.save(category);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to add because duplicate name");
            return "redirect:/categories/0";
        } catch (Exception e2) {
            e2.printStackTrace();
            attributes.addFlashAttribute("failed", "Error server");
            return "redirect:/categories/0";
        }
        attributes.addFlashAttribute("success", "Added successfully");
        return "redirect:/categories/0";
    }
    @PostMapping("/add-categoryNew")

    public String addCategoryNew(@Valid Category category,
                                 RedirectAttributes attributes){

        try{
            categorySercive.save(category);
            attributes.addFlashAttribute("success", "Added successfully");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to add because duplicate name");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error server");
        }
        return "redirect:/add-product";
    }
    @GetMapping( "/findById")
    @ResponseBody
    public Category findById(Long id) {
        return categorySercive.findById(id);
    }

    @GetMapping("/update-category") 
    public String update( Category category, RedirectAttributes redirectAttributes) {
        System.out.println(category);
        try {
            categorySercive.update(category);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        }
         catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error from server or duplicate name of category, please check again!");
        }
        return "redirect:/categories/0";
    }
    @RequestMapping(value = "/delete-category", method = {RequestMethod.GET, RequestMethod.PUT})
    public String delete(Long id, RedirectAttributes redirectAttributes) {
        try {
            categorySercive.deleteById(id);
            redirectAttributes.addFlashAttribute("Deleted", "Deleted successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/categories/0";
    }

    @RequestMapping(value = "/enable-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id, RedirectAttributes redirectAttributes) {
        try {
            categorySercive.enabledById(id);
            redirectAttributes.addFlashAttribute("Enabled", "Enable successfully");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/categories/0";

    }
    @GetMapping("/categories/{pageNo}")
    public String CategoryPage(@PathVariable("pageNo") int pageNo, Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Page<Category> categories = categorySercive.pageCategory(pageNo);
        model.addAttribute("categories",categories);
        model.addAttribute("title","Categories");
        model.addAttribute("size",categories.getSize());
        model.addAttribute("totalPages",categories.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("categoryDto",new Category());

        return "categories";
    }

    @GetMapping("/search-category/{pageNo}")
    public String searchCategory(@PathVariable("pageNo") int pageNo,@RequestParam("keyword") String keyword,
                                 Principal principal,Model model){
        if (principal == null){
            return "redirect:/login";
        }
        Page<Category> categories = categorySercive.searchCategory(keyword,pageNo);
        model.addAttribute("title","Category");
        model.addAttribute("categories",categories);
        model.addAttribute("size",categories.getSize());
        model.addAttribute("totalPages",categories.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("categoryDto",new Category());

        return "categories";
    }
}
