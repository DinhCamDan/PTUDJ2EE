package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Column(nullable = false)
    private String name;

    @Length(max = 200)
    private String image;

    @NotNull
    @Min(1)
    @Max(9999999)
    @Column(nullable = false)
    private long price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}