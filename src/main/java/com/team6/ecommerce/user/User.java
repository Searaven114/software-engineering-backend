package com.team6.ecommerce.user;


import com.team6.ecommerce.address.Address;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @Indexed
    private String email;

    private String password;
    private String name;
    private String surname;
    private String phone;
    private String age;
    private String taxId;
    private List<Address> addresses = new ArrayList<>();
    private Set<String> roles;
    private String registerIp;
    private String registerDate;
    private String cartId;
    private String wishlistId;
    private Boolean isActive = true;


    public User(String email, String password, String name, String surname, String phone, String age, String taxId, List<Address> addresses, Set<String> roles, String registerIp, String registerDate, String cartId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.age = age;
        this.taxId = taxId;
        this.addresses = addresses;
        this.roles = roles;
        this.registerIp = registerIp;
        this.registerDate = registerDate;
        this.cartId = cartId;  // Assign cartId during user creation
    }

    public User(String name, String password, String email, String phone) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}