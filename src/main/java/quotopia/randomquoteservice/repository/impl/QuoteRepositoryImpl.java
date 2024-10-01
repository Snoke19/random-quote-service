package quotopia.randomquoteservice.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import quotopia.randomquoteservice.exceptions.EntityNotFoundException;
import quotopia.randomquoteservice.models.Author;
import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.repository.QuoteRepository;

import java.util.List;

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
            log.error("Quote not found: categories: {}, errorMessage: {}",categories, e.getMessage());
            throw new EntityNotFoundException("Quote not found!", e);
        }

        return quote;
    }

    private RowMapper<Quote> mapQuote() {
        return (rs, rowNum) -> {
            Author author = new Author(rs.getString("author_name"));
            return new Quote(rs.getInt("id_quote"), rs.getString("quote_text"), author);
        };
    }
}
