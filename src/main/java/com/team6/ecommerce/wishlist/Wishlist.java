package com.team6.ecommerce.wishlist;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@NoArgsConstructor
@Data
@Document(collection = "wishlists")
public class Wishlist {

    @Id
    private String id;

    @Indexed
    private String userId;

    private Set<String> productIds;


}
