package com.ecom.admin.admin.controllers;


import com.ecom.library.library.models.Admin;
import com.ecom.library.library.service.AdminService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class AdminContoller {
    private AdminService adminService;

    public AdminContoller(AdminService adminService) {
        this.adminService = adminService;
    }


    @RequestMapping(value = {"/index","/"},method = RequestMethod.GET)
    public String homePage(Model model, Principal principal){
        if(principal!=null){
            Admin admin = adminService.findByUsername(principal.getName());
            model.addAttribute("adminName",admin.getFirstname());
        }

        model.addAttribute("title","Home Page");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
    }

}
