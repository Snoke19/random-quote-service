package quotopia.randomquoteservice.controllers;

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

    @GetMapping("/quote/random")
    public QuoteDto getRandomQuote(@RequestParam("prev_quote_id") int prevQuoteId, @RequestParam("categories") List<CategoryDto> categories) {
        Quote quote = quoteService.getRandomQuote(prevQuoteId, categories);
        return new QuoteDto(quote.getId(), quote.getQuote(), quote.getAuthor().getName());
    }
}
