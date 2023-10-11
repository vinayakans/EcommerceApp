package com.ecom.library.library.service.impl;

import com.ecom.library.library.dto.AdminDto;
import com.ecom.library.library.models.Admin;
import com.ecom.library.library.repository.AdminRepository;
import com.ecom.library.library.repository.RoleRepository;
import com.ecom.library.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminServiceImpl implements AdminService {



    private final AdminRepository adminRepository;

    private final RoleRepository roleRepository;

    public AdminServiceImpl(AdminRepository adminRepository, RoleRepository roleRepository) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;

    }

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public Admin save(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setFirstname(adminDto.getFirstname());
        admin.setLastname(adminDto.getLastname());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));

        return adminRepository.save(admin);
    }
}
