import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.subtracker.model.entity.Subscription;
import com.subtracker.model.entity.Category;
import com.subtracker.model.enums.SubscriptionStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    // Finds all subscriptions with a certain status
    List<Subscription> findByStatus(SubscriptionStatus status);

    // Finds all subscriptions whose next payment date is before a given date
    List<Subscription> findByNextPaymentDateBefore(LocalDate date);

    // Findas all  subscritpion  belonging to a given category 
    List<Subscription> findByCategory(Category category);

    // Findas all  subscritpion  belonging to a given category name
    List<Subscription> findByCategory_Name(String categoryName);

    // Finds a subscription by a given name
    Optional<Subscription> findByName(String subscriptionName);
}