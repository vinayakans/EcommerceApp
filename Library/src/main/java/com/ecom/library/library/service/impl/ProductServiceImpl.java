package com.ecom.library.library.service.impl;

import com.ecom.library.library.dto.ProductDto;
import com.ecom.library.library.models.Images;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.repository.ImageRepository;
import com.ecom.library.library.repository.ProductRepository;
import com.ecom.library.library.service.ProductService;
import com.ecom.library.library.util.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {


    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository, ImageRepository imageRepository, ImageUpload imageUpload) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.imageUpload = imageUpload;
    }

    private ImageRepository imageRepository;
    private ImageUpload imageUpload;

    @Override
    public List<ProductDto> findAll() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for(Product product:products){
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCategory(product.getCategory());
//            productDto.setProductImage(product.getProductImage());
            productDto.set_activated(product.is_activated());
            productDto.set_deleted(product.is_deleted());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public Product save(List<MultipartFile> imageProducts, ProductDto productDto) {
        try {
            Product product = new Product();

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(false);
            product.set_deleted(false);

            Product savedProduct = productRepository.save(product);

            if(imageProducts==null){
                product.setProductImage(null);
            }else{
                List<Images> imagesList = new ArrayList<>();
               for (MultipartFile imageProduct :  imageProducts){
                   Images images = new Images();
                   String imageName = imageUpload.uploadImage(imageProduct);
                   images.setName(imageName);
                   images.setProduct(product);
                   imageRepository.save(images);
                   imagesList.add(images);
               }
               product.setProductImage(imagesList);
            }
            return savedProduct;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("save Service");
            return null;
        }


    }

    @Override
    public Product update(List<MultipartFile> ImageProducts,ProductDto productDto,Long id) {
        try{
            Product productUpdate = productRepository.getReferenceById(id);

            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setSalePrice(productDto.getSalePrice());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            productUpdate.setCategory(productDto.getCategory());
            Product savedProduct = productRepository.save(productUpdate);

            if (ImageProducts != null && !ImageProducts.isEmpty() && ImageProducts.size()!=1){
                List<Images> imagesList = new ArrayList<>();
                List <Images> images = imageRepository.findImagesBy(id);
                int i=0;
                for (MultipartFile imageProduct : ImageProducts){
                    String ImageName = imageUpload.uploadImage(imageProduct);
                    images.get(i).setName(ImageName);
                    images.get(i).setProduct(savedProduct);
                    imageRepository.save(images.get(i));
                    imagesList.add(images.get(i));
                    i++;
                }
                productUpdate.setProductImage(imagesList);
            }
            return savedProduct;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void enableProduct(Long id) {
        Product product = productRepository.getReferenceById(id);
     try {
         if(product.is_activated()){
             product.set_activated(false);
         }else {
             product.set_activated(true);
         }
     }catch (Exception e){
         e.printStackTrace();
     }
    }

    @Override
    public Product findById(Long id) {
        Product product = productRepository.getReferenceById(id);
        return product;
    }

    @Override
    public List<Product> findAllByActive() {
      List<Product> productList = productRepository.findAllByActive();
        return productList;
    }

    @Override
    public List<Product> findAllNotDeleted() {
        List<Product> productList = productRepository.findAllByNotDeleted();
       return productList;
    }

    @Override
    public Page<Product> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo,5);
        Page<Product> productsPage = productRepository.pageProduct(pageable);
        return productsPage;
    }

    @Override
    public Page<Product> searchProducts(String keyword,int pageNo) {
        Pageable pageable = PageRequest.of(pageNo,5);
        Page<Product> products = productRepository.searchProducts(keyword,pageable);
        return products;
    }

    @Override
    public List<Product> findByCategory(Long id) {
      List< Product >product =  productRepository.findBYCategoryId(id);
        return product;
    }

    @Override
    public void softDeleteById(long id) {
        try{
            System.out.println(id);
            Product products = productRepository.findById(id);
//            System.out.println(products.is_deleted());
            if(products.is_deleted() == false){
                products.set_deleted(true);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.getReferenceById(id);
        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setProductImage(product.getProductImage());
        productDto.setCategory(product.getCategory());
        productDto.set_activated(product.is_deleted());
        productDto.set_deleted(product.is_deleted());

        return productDto;
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.getReferenceById(id);
        return product;
    }
}
