package com.ecom.library.library.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Component
public class ImageUpload {

    String rootPath = System.getProperty("user.home");
    String UPLOAD_FOLDER ="C:\\Users\\vinay\\IdeaProjects\\EcommerceApp\\Admin\\src\\main\\resources\\static\\img\\image-product";
    String UPLOAD_FOLDER_CUSTOMER = "C:\\Users\\vinay\\IdeaProjects\\EcommerceApp\\Customer\\src\\main\\resources\\static\\img\\images";
//    private  final byte[] UPLOAD_FOLDER = "C:\\Users\\vinay\\IdeaProjects\\EcommerceApp\\Admin\\src\\main\\resources\\static\\img\\image-product".getBytes();
//    private  final byte[] CUSTOMER_FOLDER = "C:\\Users\\vinay\\IdeaProjects\\EcommerceApp\\Customer\\src\\main\\resources\\static\\img\\images".getBytes();
    public  String uploadImage(MultipartFile file)throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get(UPLOAD_FOLDER);
        Path uploadPathCustomer = Paths.get(UPLOAD_FOLDER_CUSTOMER);

        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
            Files.createDirectories(uploadPathCustomer);
        }

        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            Path filePath = uploadPath.resolve(fileName);
            Path filePathCustomer = uploadPathCustomer.resolve(fileName);
            Files.write(filePath,buffer, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(filePathCustomer,buffer, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            return fileName.toString();
        } catch (IOException e) {
            throw new IOException("Could not store file " + fileName, e);
        }
    }


    public boolean checkExist(MultipartFile File){
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "/" + File.getOriginalFilename());
            isExisted= file.exists();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isExisted;
    }

}
