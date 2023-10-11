package com.ecom.library.library.service;

import com.ecom.library.library.dto.AdminDto;
import com.ecom.library.library.models.Admin;

public interface AdminService {
    Admin findByUsername(String username);
    Admin save(AdminDto adminDto);
}
