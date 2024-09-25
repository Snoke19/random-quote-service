package quotopia.randomquoteservice.service;

import org.springframework.stereotype.Service;
import quotopia.randomquoteservice.repository.QuoteRepository;
import quotopia.randomquoteservice.dto.CategoryDto;
import quotopia.randomquoteservice.models.Quote;

import java.util.List;
import java.util.Optional;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public Quote getRandomQuote(int prevQuoteId, List<CategoryDto> categoryDtos) {

        List<String> categories = categoryDtos.stream().map(CategoryDto::name).toList();
        Optional<Quote> quoteOptional = this.quoteRepository.findRandomQuoteByCategoriesExcludingPrevId(prevQuoteId, categories);

        return quoteOptional.orElse(null);
    }
}
