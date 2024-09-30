package quotopia.randomquoteservice.service;

import org.springframework.stereotype.Service;
import quotopia.randomquoteservice.dto.CategoryDto;
import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.repository.QuoteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public Quote getRandomQuote(int prevQuoteId, List<CategoryDto> categories) {

        List<String> categoriesStr = categories.stream()
                .map(data -> data.name().toLowerCase())
                .filter(data -> !data.isEmpty())
                .toList();
        Optional<Quote> quoteOptional = this.quoteRepository.findRandomQuoteByCategoriesExcludingPrevId(prevQuoteId, categoriesStr);

        return quoteOptional.orElse(null);
    }
}
