package quotopia.randomquoteservice.repository.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import quotopia.randomquoteservice.models.Category;
import quotopia.randomquoteservice.repository.CategoryRepository;

import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CategoryRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Category> findFirst10CategoriesByNameContainingOrderByNameAscWithOffset(String categoryName, int offset) {
        String sql = """
                        select category.name as name
                        from categories category
                        where (COALESCE(:name, '') IS NULL OR category.name like concat('%', COALESCE(:name, ''), '%') escape '\\')
                        order by category.name
                        offset :offset rows fetch first 10 rows only
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", categoryName);
        params.addValue("offset", offset);

        return this.namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> new Category(rs.getString("name").toLowerCase()));
    }
}
