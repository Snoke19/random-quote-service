package quotopia.randomquoteservice.repository;

import quotopia.randomquoteservice.models.Quote;

import java.util.List;

public interface QuoteRepository {

    Quote findRandomQuoteByCategoriesExcludingPreviousId(int previousQuoteId, List<String> categories);
}
