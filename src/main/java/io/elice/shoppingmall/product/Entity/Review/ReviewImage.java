package io.elice.shoppingmall.product.Entity.Review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String filePath;
    private String fileName;

    @OneToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
