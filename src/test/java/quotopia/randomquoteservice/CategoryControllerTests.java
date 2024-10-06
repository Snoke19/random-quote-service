package quotopia.randomquoteservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import quotopia.randomquoteservice.controllers.error.ResponseError;
import quotopia.randomquoteservice.models.Category;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    static {
        postgreSQLContainer.withInitScript("schema.sql");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test_get_all_categories() {
        ResponseEntity<List<Category>> responseEntity = restTemplate.exchange(
                "/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        List<Category> categories = responseEntity.getBody();
        assertThat(categories).hasSize(10);

        assertThat(categories.get(0)).hasFieldOrPropertyWithValue("name", "courage");
        assertThat(categories.get(1)).hasFieldOrPropertyWithValue("name", "friendship");
        assertThat(categories.get(2)).hasFieldOrPropertyWithValue("name", "happiness");
    }

    @Test
    void test_get_categories_by_name() {
        String categoryName = "friendship";
        ResponseEntity<List<Category>> responseEntity = restTemplate.exchange(
                "/categories/" + categoryName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        List<Category> categories = responseEntity.getBody();
        assertThat(categories).isNotEmpty();
        assertThat(categories.get(0).getName()).isEqualTo(categoryName);
    }

    @Test
    void test_get_categories_by_name_short_style() {
        ResponseEntity<List<Category>> responseEntity = restTemplate.exchange(
                "/categories/in",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        List<Category> categories = responseEntity.getBody();
        assertThat(categories).hasSizeGreaterThanOrEqualTo(2);
        assertThat(categories.get(0)).hasFieldOrPropertyWithValue("name", "happiness");
        assertThat(categories.get(1)).hasFieldOrPropertyWithValue("name", "inspirational");
    }

    @Test
    void test_get_categories_with_offset() {
        int offset = 0;
        ResponseEntity<List<Category>> responseEntity = restTemplate.exchange(
                "/categories?offset=" + offset,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        List<Category> categories = responseEntity.getBody();
        assertThat(categories).hasSize(10);

        assertThat(categories.get(0)).hasFieldOrPropertyWithValue("name", "courage");
        assertThat(categories.get(1)).hasFieldOrPropertyWithValue("name", "friendship");
        assertThat(categories.get(2)).hasFieldOrPropertyWithValue("name", "happiness");
        assertThat(categories.get(3)).hasFieldOrPropertyWithValue("name", "humor");
        assertThat(categories.get(4)).hasFieldOrPropertyWithValue("name", "inspirational");
        assertThat(categories.get(5)).hasFieldOrPropertyWithValue("name", "life");
        assertThat(categories.get(6)).hasFieldOrPropertyWithValue("name", "love");
        assertThat(categories.get(7)).hasFieldOrPropertyWithValue("name", "motivational");
        assertThat(categories.get(8)).hasFieldOrPropertyWithValue("name", "success");
        assertThat(categories.get(9)).hasFieldOrPropertyWithValue("name", "wisdom");

        int offset2 = 10;
        ResponseEntity<List<Category>> responseEntity2 = restTemplate.exchange(
                "/categories?offset=" + offset2,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity2).isNotNull();
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity2.getBody()).isNotNull();

        List<Category> categories2 = responseEntity2.getBody();
        assertThat(categories2).hasSize(1);

        assertThat(categories2.get(0)).hasFieldOrPropertyWithValue("name", "wisdom1");
    }

    @Test
    void test_get_categories_with_invalid_offset() {
        int invalidOffset = -1;
        ResponseEntity<ResponseError> responseEntity = restTemplate.exchange(
                "/categories?offset=" + invalidOffset,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        Map<String, String> map = (Map<String, String>) responseEntity.getBody().getDetails();
        assertThat(map).containsEntry("offset", "Offset cannot be negative!");
    }
}
