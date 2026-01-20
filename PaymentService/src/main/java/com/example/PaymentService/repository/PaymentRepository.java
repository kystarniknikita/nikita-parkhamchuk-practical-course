package com.example.PaymentService.repository;

import com.example.PaymentService.model.entity.Payment;
import com.example.PaymentService.model.entity.type.StatusType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByUserId(Long userId);

    List<Payment> findByStatusIn(List<StatusType> statuses);

    List<Payment> findByTimestampBetween(Instant startDate, Instant endDate);

    @Query("{ 'timestamp': { $gte: ?0, $lte: ?1 } }")
    List<Payment> findPaymentsInDateRange(Instant startDate, Instant endDate);

    @Aggregation(pipeline = {
            "{ $match: { timestamp: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: null, totalAmount: { $sum: '$paymentAmount' }, count: { $sum: 1 } } }"
    })
    PaymentSummaryResult getTotalPaymentsForDatePeriod(Instant startDate, Instant endDate);

    interface PaymentSummaryResult {
        BigDecimal getTotalAmount();

        Long getCount();
    }
}
