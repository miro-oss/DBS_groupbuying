package com.example.groupbuying.domain.groupbuy.enums;

public enum PaymentStatus {
    WAITING("입금대기"),
    CONFIRMED("입금확인"),
    SHIPPING("배송중"),
    COMPLETED("거래완료"),
    CANCELED("거래취소");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
