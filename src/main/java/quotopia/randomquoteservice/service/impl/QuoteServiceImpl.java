package quotopia.randomquoteservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.models.QuoteFull;
import quotopia.randomquoteservice.repository.QuoteRepository;
import quotopia.randomquoteservice.service.QuoteService;

import java.util.List;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Quote getRandomQuote(List<Category> categories) {
        List<String> categoryNames = categories.stream()
                .map(category -> category.getName().toLowerCase())
                .filter(name -> !name.isEmpty())
                .toList();

        return this.quoteRepository.findRandomQuoteByCategoriesExcludingPreviousId(categoryNames);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuoteFull> getQuotesByTextWithOffset(String textQuote, int offset) {
        if (textQuote != null) {
            textQuote = textQuote.toLowerCase();
        }
        return this.quoteRepository.findQuotesByTextWithOffset(textQuote, offset);
    }
}
