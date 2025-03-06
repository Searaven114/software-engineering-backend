package com.team6.ecommerce.rating;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "ratings")
public class Rating {

    @Id
    private String id;

    @Indexed
    private String productId;

    private String userId;

    private int rating; // Rating between 1 and 5
}
