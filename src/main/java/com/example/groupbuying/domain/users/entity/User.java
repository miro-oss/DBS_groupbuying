package com.example.groupbuying.domain.users.entity;

import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.entity.Submission;
import com.example.groupbuying.global.entity.BaseEntity;
import jakarta.persistence.*;
import  lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Builder.Default
    @OneToMany(mappedBy = "seller")
    private List<Form> sellingForms = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "buyer")
    private List<Submission> submissions = new ArrayList<>();

    public void addSellingForm(Form form) {
        this.sellingForms.add(form);
        form.setSeller(this);  // 양방향 동기화
    }

    public void addSubmission(Submission submission) {
        this.submissions.add(submission);
        submission.setBuyer(this); // 양방향 동기화
    }

}
