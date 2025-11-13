package com.example.groupbuying.domain.groupbuy.entity;

import com.example.groupbuying.domain.groupbuy.enums.PaymentStatus;
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "submissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_submission_form_buyer",
                        columnNames = {"form_id", "buyer_id"}
                )
        }
)
@AttributeOverride(
        name = "createdAt",
        column = @Column(name = "submitted_at")
)
@EntityListeners(AuditingEntityListener.class)
public class Submission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Column(name = "buyer_name", nullable = false, length = 50)
    private String buyerName;

    @Column(name = "buyer_contact", nullable = false, length = 30)
    private String buyerContact;

    @Builder.Default
    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.WAITING;

    public void setForm(Form form) {
        this.form = form;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }
}
