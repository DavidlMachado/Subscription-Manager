import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.subtracker.model.entity.PaymentRecord;
import com.subtracker.model.entity.Subscription;
import com.subtracker.model.enums.PaymentStatus;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Integer> {
    // Finds all payment records for subscriptions belonging to a given category
    List<PaymentRecord> findBySubscription_Category(Category category);

    // Finds all payment records for subscriptions belonging to a given category name
    List<PaymentRecord> findBySubscription_Category_Name(String categoryName);

    // Finds all payment records for a given subscription
    List<PaymentRecord> findBySubscription(Subscription subscription);

    // Finds all payment records for a given subscription name
    List<PaymentRecord> findBySubscription_Name(String subscriptionName);

    // Finds all payment records between two dates
    List<PaymentRecord> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    // Finds all payment records for a given subscription between two dates
    List<PaymentRecord> findBySubscriptionAndDueDateBetween(Subscription subscription, LocalDate startDate, LocalDate endDate);

    // Finds all payment records for a given subscription name between two dates
    List<PaymentRecord> findBySubscription_NameAndDueDateBetween(String subscriptionName, LocalDate startDate, LocalDate endDate);

    // Finds all payment records with a given status (PENDING, PAID, LATE, MISSED)
    List<PaymentRecord> findByStatus(PaymentStatus status);

    // Finds all payment records with a given status for a given subscription
    List<PaymentRecord> findBySubscriptionAndStatus(Subscription subscription, PaymentStatus status);

    // Finds all missed or late payments (useful for a "problem payments" dashboard)
    List<PaymentRecord> findByStatusIn(List<PaymentStatus> statuses);
}