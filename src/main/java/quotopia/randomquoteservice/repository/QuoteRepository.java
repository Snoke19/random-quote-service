package quotopia.randomquoteservice.repository;

import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.models.QuoteFull;

import java.util.List;

public interface QuoteRepository {

    Quote findRandomQuoteByCategoriesExcludingPreviousId(List<String> categories);
    List<QuoteFull> findQuotesByTextWithOffset(String textQuote, int offset);
}
