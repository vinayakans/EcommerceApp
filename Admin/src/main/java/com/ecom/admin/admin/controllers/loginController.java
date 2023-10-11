package com.ecom.admin.admin.controllers;

import com.ecom.library.library.dto.AdminDto;
import com.ecom.library.library.models.Admin;
import com.ecom.library.library.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;

@Controller
public class loginController {

    private final PasswordEncoder passwordEncoder;

    private final AdminService adminService;

    public loginController(AdminService adminService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title" , "Login ");
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(){
        return "forgot-password";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("adminDto",new AdminDto());
        return "register";
    }
    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto")AdminDto adminDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        try{
            if(result.hasErrors()){
                result.toString();
                model.addAttribute("adminDto",adminDto);
                System.out.println("1");
                return "register";
            }
            String username = adminDto.getUsername();
            Admin user = adminService.findByUsername(username);
            if(user != null){
                System.out.println("3");
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("emailError", "Your email is already used!");

                return "register";
            }
            if(adminDto.getPassword().equals(adminDto.getRepeatpassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                redirectAttributes.addFlashAttribute("success", "Registered successfully!");
                model.addAttribute("adminDto",adminDto);

            }else {
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("passwordError", "Your password maybe wrong! Check again!");

                return "register";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error","error, please try again later");

        }

        return "redirect:/register";
    }
}
