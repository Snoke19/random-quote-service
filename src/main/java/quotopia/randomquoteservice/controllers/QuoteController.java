package quotopia.randomquoteservice.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quotopia.randomquoteservice.dto.CategoryDto;
import quotopia.randomquoteservice.dto.QuoteDto;
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
    public QuoteDto getRandomQuote(@RequestParam("categories")
                                   @NotEmpty(message = "{quote.filter.notEmpty.categories}") List<@Valid CategoryDto> categories,
                                   @RequestParam(value = "prev_quote_id", defaultValue = "0")
                                   @Min(value = 0, message = "{quote.filter.min.prevQuoteId}") int prevQuoteId) {
        Quote quote = quoteService.getRandomQuote(prevQuoteId, categories);
        return new QuoteDto(quote.getId(), quote.getQuote(), quote.getAuthor().getName());
    }
}
