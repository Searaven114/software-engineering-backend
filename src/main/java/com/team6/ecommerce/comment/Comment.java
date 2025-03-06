package com.team6.ecommerce.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @Indexed
    private String productId;

    private String userId;

    @NotBlank
    private String content;

    private boolean approved = false;

    private LocalDateTime createdDate;
}
