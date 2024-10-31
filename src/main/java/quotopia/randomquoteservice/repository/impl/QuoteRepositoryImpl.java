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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        String sqlQuotes = """
                SELECT quote.id_quote,
                       quote.quote_text,
                       author.id_author AS id_author,
                       author.name AS author_name
                FROM quotes AS quote
                        INNER JOIN public.authors author ON author.id_author = quote.author_id
                WHERE quote_text ILIKE :textQuote
                ORDER BY quote.id_quote
                OFFSET :offset ROWS FETCH FIRST 10 ROWS ONLY;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("textQuote", "%" + textQuote + "%");
        params.addValue("offset", offset);

        List<Quote> quotes;
        try {
            quotes = this.namedParameterJdbcTemplate.query(sqlQuotes, params, mapQuotes());
        } catch (EmptyResultDataAccessException e) {
            log.error("Quotes not found: textQuote: {}, errorMessage: {}", textQuote, e.getMessage());
            throw new EntityNotFoundException("Quotes not found!", e);
        }

        if (quotes != null && !quotes.isEmpty()) {

            String sqlCategories = """
                    SELECT  cq.quote_id          AS quote_id,
                            category.id_category AS id_category,
                            category.name        AS category_name
                    FROM quotes AS quote
                            JOIN categories_quotes cq ON quote.id_quote = cq.quote_id
                            JOIN public.categories category ON category.id_category = cq.category_id
                    WHERE cq.quote_id IN (:quoteIds)
                    ORDER BY quote.id_quote;
                    """;

            MapSqlParameterSource paramsCategories = new MapSqlParameterSource();
            paramsCategories.addValue("quoteIds", quotes.stream().map(Quote::getId).collect(Collectors.toSet()));

            Map<Integer, List<Category>> categoriesMap;
            try {
                categoriesMap = this.namedParameterJdbcTemplate.query(sqlCategories, paramsCategories, mapCategories());
            } catch (EmptyResultDataAccessException e) {
                log.error("Categories not found: textQuote: {}, errorMessage: {}", textQuote, e.getMessage());
                throw new EntityNotFoundException("Categories not found!", e);
            }

            List<QuoteFull> quotesFull = new ArrayList<>();
            if (categoriesMap != null && !categoriesMap.isEmpty()) {
                for (int i = 0; i < quotes.size(); i++) {
                    Quote quote = quotes.get(i);
                    List<Category> categories = categoriesMap.getOrDefault(quote.getId(), Collections.emptyList());
                    QuoteFull quoteFull = new QuoteFull(quote.getId(), quote.getQuoteText(), quote.getAuthor(), categories);
                    quotesFull.add(quoteFull);
                }
            }

            return quotesFull;
        }

        return Collections.emptyList();
    }

    private RowMapper<Quote> mapQuote() {
        return (rs, rowNum) -> prepareQuote(rs);
    }

    private ResultSetExtractor<List<Quote>> mapQuotes() {
        return rs -> {
            List<Quote> quotes = new ArrayList<>();
            while (rs.next()) {
                Quote quote = prepareQuote(rs);
                quotes.add(quote);
            }
            return quotes;
        };
    }

    private ResultSetExtractor<Map<Integer, List<Category>>> mapCategories() {
        return rs -> {
            Map<Integer, List<Category>> categoriesMap = new HashMap<>();
            while (rs.next()) {
                int quoteId = rs.getInt("quote_id");
                List<Category> categories = categoriesMap.get(quoteId);

                Category category = prepareCategory(rs);
                if (categories == null) {
                    List<Category> newCategories = new ArrayList<>();
                    newCategories.add(category);
                    categoriesMap.put(quoteId, newCategories);
                } else {
                    categoriesMap.get(quoteId).add(category);
                }
            }
            return categoriesMap;
        };
    }

    private Category prepareCategory(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("id_category"), rs.getString("category_name"));
    }

    private Quote prepareQuote(ResultSet rs) throws SQLException {
        Author author = new Author(rs.getInt("id_author"), rs.getString("author_name"));
        return new Quote(rs.getInt("id_quote"), rs.getString("quote_text"), author);
    }
}
