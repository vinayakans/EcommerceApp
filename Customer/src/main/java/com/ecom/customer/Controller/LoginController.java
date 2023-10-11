package com.ecom.customer.Controller;

import com.ecom.library.library.dto.CustomerDto;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class LoginController {
    private PasswordEncoder passwordEncoder;

    private final CustomerService customerService;

    public LoginController(CustomerService customerService,PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session){

        model.addAttribute("title","Login Form");
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("customerDto",new CustomerDto());
        return "register";
    }

    @PostMapping("/register-new")
    public String addNewCustomer(@Valid @ModelAttribute("customerDto")CustomerDto customerDto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes
                                 ){
            try{
                if (result.hasErrors()){
                    result.toString();
                    model.addAttribute("customerDto",customerDto);
                    return "register";
                }
                String Username =customerDto.getUsername();
                Customer user = customerService.findByUsername(Username);
                if(user!=null){
                    model.addAttribute("emailError","Email already exists");
                    model.addAttribute("customerDto",customerDto);
                    return "register";
                }
                if(customerDto.getPassword().equals(customerDto.getRepeatPassword())){
                        customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                        customerService.save(customerDto);
                        redirectAttributes.addFlashAttribute("success", "Registration successfull!!!!");

                }else {
                    model.addAttribute("passwordError","Password not same!!");
                    model.addAttribute("customerDto",customerDto);
                    return "register";
                }


            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("error","Can't Connect At this moment!!!");
            }

        return "redirect:/register";
    }

}
