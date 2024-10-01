package quotopia.randomquoteservice.service;

import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.models.Quote;

import java.util.List;

public interface QuoteService {

    Quote getRandomQuote(List<Category> categories);
}
