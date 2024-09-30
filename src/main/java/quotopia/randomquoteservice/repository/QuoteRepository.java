package quotopia.randomquoteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import quotopia.randomquoteservice.models.Quote;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM quotes as q " +
            " join public.categories_quotes cq on q.id_quote = cq.quote_id " +
            " left join public.categories c on c.id_category = cq.category_id " +
            " where (:id IS NULL OR q.id_quote != :id) and c.name in (:categories) " +
            " ORDER BY RANDOM() LIMIT 1;")
    Optional<Quote> findRandomQuoteByCategoriesExcludingPrevId(@Param("id") int prevQuoteId, @Param("categories") List<String> categories);
}
