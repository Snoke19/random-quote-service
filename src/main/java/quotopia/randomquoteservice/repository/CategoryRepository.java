package quotopia.randomquoteservice.repository;

import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import quotopia.randomquoteservice.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Window<Category> findFirst10ByNameContaining(String name, ScrollPosition position);
}
