package quotopia.randomquoteservice.service;

import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.models.QuoteFull;

import java.util.List;

public interface QuoteService {

    Quote getRandomQuote(List<Category> categories);
    List<QuoteFull> getQuotesByTextWithOffset(String textQuote, int offset);
}
