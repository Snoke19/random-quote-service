package quotopia.randomquoteservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import quotopia.randomquoteservice.models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(nativeQuery = true, value = "select c1_0.id_category, c1_0.name " +
            "from categories c1_0 " +
            "where c1_0.name like concat('%', :name, '%') escape '\\' " +
            "order by c1_0.name " +
            "offset :offset rows fetch first 10 rows only")
    List<Category> findFirst10ByNameContainingOrderByNameAsc(@Param("name") String name, @Param("offset") int offset);
}
