package com.example.groupbuying.domain.groupbuy.entity;

import com.example.groupbuying.domain.groupbuy.enums.FormStatus;
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "forms", indexes = {
        // 1. 마감 임박 상품 조회 최적화
        @Index(name = "idx_form_status_deadline", columnList = "status, deadline"),

        // 2. 카테고리별 상품 리스트 조회 최적화
        @Index(name = "idx_form_category", columnList = "category_id"),

        // 3. 특정 판매자의 폼 조회 최적화
        @Index(name = "idx_form_seller", columnList = "seller_id")
})
@EntityListeners(AuditingEntityListener.class)
public class Form extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions = new ArrayList<>();

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_per_unit", nullable = false)
    private BigDecimal pricePerUnit;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "trade_time")
    private LocalDateTime tradeTime;

    @Column(name = "account_bank", length = 30, nullable = false)
    private String accountBank;

    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;

    @Column(name = "account_name", length = 50, nullable = false)
    private String accountName;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormStatus status = FormStatus.OPEN;

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void addSubmission(Submission submission) {
        this.submissions.add(submission);
        submission.setForm(this);   // 양방향 동기화
    }

    public void updateForm(
            String title,
            String description,
            BigDecimal pricePerUnit,
            String imageUrl,
            LocalDateTime orderDate,
            String location,
            LocalDateTime tradeTime,
            String accountBank,
            String accountNumber,
            String accountName,
            LocalDateTime deadline,
            Category category
    ) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (pricePerUnit != null) this.pricePerUnit = pricePerUnit;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (orderDate != null) this.orderDate = orderDate;
        if (location != null) this.location = location;
        if (tradeTime != null) this.tradeTime = tradeTime;
        if (accountBank != null) this.accountBank = accountBank;
        if (accountNumber != null) this.accountNumber = accountNumber;
        if (accountName != null) this.accountName = accountName;
        if (deadline != null) this.deadline = deadline;
        if (category != null) this.category = category;
    }

    public void close() {
        this.status = FormStatus.CLOSED;
    }
    public void reopen() {this.status = FormStatus.OPEN;   }
}
