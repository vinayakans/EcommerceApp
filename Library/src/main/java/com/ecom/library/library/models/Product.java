package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.Set;

@Getter@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product",uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    private double costPrice;

    private double salePrice;

    private int currentQuantity;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "product",cascade = {CascadeType.MERGE,
            CascadeType.PERSIST,CascadeType.DETACH,CascadeType.REFRESH})
    private List<Images> productImage;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="category_id",referencedColumnName = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<CartItem> cartItemList;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> reviews;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.MERGE,CascadeType.PERSIST,
            CascadeType.REFRESH,CascadeType.DETACH})
    private List<OrderDetails> orderDetails;

    private boolean is_deleted;

    private boolean is_activated;

}
