package quotopia.randomquoteservice.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quotopia.randomquoteservice.dto.CategoryIdDto;
import quotopia.randomquoteservice.service.CategoryService;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping({"/categories/{name}", "/categories"})
    public List<CategoryIdDto> getCategoriesByName(@PathVariable(value = "name", required = false) String name,
                                                   @Min(value = 0, message = "{category.filter.min.offset}")
                                                   @RequestParam(value = "offset", defaultValue = "0")
                                                   int offset) {
        return this.categoryService.getCategoriesByName(name, offset);
    }
}