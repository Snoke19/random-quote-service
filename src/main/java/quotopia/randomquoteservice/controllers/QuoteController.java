package quotopia.randomquoteservice.controllers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.service.QuoteService;

import java.util.List;

@RestController
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/quote")
    public Quote getRandomQuote(@RequestParam("categories")
                                @NotEmpty(message = "{quote.filter.notEmpty.categories}") List<Category> categories,
                                @RequestParam(value = "prev_quote_id", defaultValue = "0")
                                @Min(value = 0, message = "{quote.filter.min.prevQuoteId}") int previousQuoteId) {
        return quoteService.getRandomQuote(previousQuoteId, categories);
    }
}
