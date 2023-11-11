package com.ecom.library.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="customers", uniqueConstraints = @UniqueConstraint(columnNames = {"username","phone_number"}))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;
    @Size(min=3,max = 15,message ="first name should have 3-15 characters" )
    private String firstName;
    @Size(min=3,max = 15,message ="last name should have 3-15 characters" )

    private String lastName;

    @Column(name = "username")
    private String username;

    private String password;

    @Column(name="phone_number")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name="customer_roles",
            joinColumns = @JoinColumn(name="customer_id",referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "role_id"))
    private Collection<Role> roles;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Order> orders;


   @OneToMany(mappedBy = "customer")
    private List<Address> address;

    private boolean is_blocked = false;

    @OneToMany(mappedBy = "customer")
    private List<ProductReview> reviews;

    @ToString.Exclude
    @OneToOne(mappedBy = "customer")
    private Wallet wallet;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", is_blocked=" + is_blocked +
                '}';
    }

}