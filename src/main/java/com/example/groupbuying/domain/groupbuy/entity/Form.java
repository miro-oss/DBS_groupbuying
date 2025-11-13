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
@Table(name = "forms")
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
}
