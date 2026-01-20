package com.example.PaymentService.model.entity;

import com.example.PaymentService.model.entity.type.StatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    private String id;

    @Field("orderId")
    @Indexed
    private Long orderId;

    @Field("userId")
    @Indexed
    private Long userId;

    @Field("status")
    @Indexed
    private StatusType status;

    @Field("timestamp")
    @CreatedDate
    @Indexed
    private Instant timestamp;

    @Field("paymentAmount")
    private BigDecimal paymentAmount;
}
