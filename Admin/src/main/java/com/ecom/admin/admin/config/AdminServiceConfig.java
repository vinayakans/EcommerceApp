package com.ecom.admin.admin.config;

import com.ecom.library.library.models.Admin;
import com.ecom.library.library.repository.AdminRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class AdminServiceConfig implements UserDetailsService {



    private AdminRepository adminRepository;

    public AdminServiceConfig(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);
        if(admin == null){
            throw new UsernameNotFoundException("Could not find username");
        }
        return new User(
                admin.getUsername(),
                admin.getPassword(),
                admin.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }
}
