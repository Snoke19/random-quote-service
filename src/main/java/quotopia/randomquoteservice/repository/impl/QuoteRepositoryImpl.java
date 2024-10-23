package quotopia.randomquoteservice.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import quotopia.randomquoteservice.exceptions.EntityNotFoundException;
import quotopia.randomquoteservice.models.Author;
import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.models.QuoteFull;
import quotopia.randomquoteservice.repository.QuoteRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class QuoteRepositoryImpl implements QuoteRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public QuoteRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Quote findRandomQuoteByCategoriesExcludingPreviousId(List<String> categories) {

        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("Categories cannot be null or empty");
        }

        String sql = """
                SELECT quote.id_quote, quote.quote_text, author.id_author AS id_author, author.name AS author_name
                FROM quotes AS quote
                JOIN categories_quotes cq ON quote.id_quote = cq.quote_id
                INNER JOIN public.authors author ON author.id_author = quote.author_id
                JOIN public.categories c ON c.id_category = cq.category_id
                AND c.name IN (:categories)
                ORDER BY RANDOM() LIMIT 1;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categories", categories);

        Quote quote;
        try {
            quote = this.namedParameterJdbcTemplate.queryForObject(sql, params, mapQuote());
        } catch (EmptyResultDataAccessException e) {
            log.error("Quote not found: categories: {}, errorMessage: {}", categories, e.getMessage());
            throw new EntityNotFoundException("Quote not found!", e);
        }

        return quote;
    }

    @Override
    public List<QuoteFull> findQuotesByTextWithOffset(String textQuote, int offset) {
        if (textQuote == null) {
            throw new IllegalArgumentException("Text quote cannot be null!");
        }

        String sql = """
                SELECT quote.id_quote,
                       quote.quote_text,
                       author.id_author AS id_author,
                       author.name AS author_name,
                       category.id_category as id_category,
                       category.name AS category_name
                FROM quotes AS quote
                JOIN categories_quotes cq ON quote.id_quote = cq.quote_id
                INNER JOIN public.authors author ON author.id_author = quote.author_id
                JOIN public.categories category ON category.id_category = cq.category_id
                AND quote.quote_text like :textQuote
                order by quote.id_quote
                offset :offset rows fetch first 10 rows only;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        String textQuoteWildcards = "%" + textQuote + "%";
        params.addValue("textQuote", textQuoteWildcards);
        params.addValue("offset", offset);

        List<QuoteFull> quotes;
        try {
            quotes = this.namedParameterJdbcTemplate.query(sql, params, mapFullQuote());
        } catch (EmptyResultDataAccessException e) {
            log.error("Quotes not found: textQuote: {}, errorMessage: {}", textQuote, e.getMessage());
            throw new EntityNotFoundException("Quotes not found!", e);
        }

        return quotes;
    }

    private RowMapper<Quote> mapQuote() {
        return (rs, rowNum) -> {
            Author author = new Author(rs.getInt("id_author"), rs.getString("author_name"));
            return new Quote(rs.getInt("id_quote"), rs.getString("quote_text"), author);
        };
    }

    private ResultSetExtractor<List<QuoteFull>> mapFullQuote() {
        return rs -> {
            Map<Integer, QuoteFull> quoteMap = new HashMap<>();
            while (rs.next()) {
                int quoteId = rs.getInt("id_quote");
                QuoteFull quote = quoteMap.get(quoteId);

                if (quote == null) {
                    Author author = new Author(rs.getInt("id_author"), rs.getString("author_name"));
                    quote = new QuoteFull(quoteId, rs.getString("quote_text"), author);
                    quoteMap.put(quoteId, quote);
                }

                Category category = new Category();
                category.setId(rs.getInt("id_category"));
                category.setName(rs.getString("category_name"));
                quote.getCategories().add(category);
            }
            return new ArrayList<>(quoteMap.values());
        };
    }
}
