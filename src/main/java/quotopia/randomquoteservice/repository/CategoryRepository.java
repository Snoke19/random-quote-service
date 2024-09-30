package quotopia.randomquoteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import quotopia.randomquoteservice.models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(nativeQuery = true, value = "select category.id_category, category.name " +
            "from categories category " +
            "where (:name IS NULL OR category.name like concat('%', :name, '%') escape '\\') " +
            "order by category.name " +
            "offset :offset rows fetch first 10 rows only")
    List<Category> findFirst10ByNameContainingOrderByNameAsc(@Param("name") String name, @Param("offset") int offset);
}
