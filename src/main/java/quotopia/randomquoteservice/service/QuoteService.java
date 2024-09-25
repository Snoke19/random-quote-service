package quotopia.randomquoteservice.service;

import quotopia.randomquoteservice.dto.CategoryDto;
import quotopia.randomquoteservice.models.Quote;

import java.util.List;

public interface QuoteService {

    Quote getRandomQuote(int prevQuoteId, List<CategoryDto> categories);
}
