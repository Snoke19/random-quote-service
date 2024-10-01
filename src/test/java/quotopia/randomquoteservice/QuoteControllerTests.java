package quotopia.randomquoteservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import quotopia.randomquoteservice.controllers.error.ResponseError;
import quotopia.randomquoteservice.models.Quote;
import quotopia.randomquoteservice.repository.impl.QuoteRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuoteControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    static {
        postgreSQLContainer.withInitScript("schema.sql");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private QuoteRepositoryImpl quoteRepository;

    @Test
    void test_get_a_random_quote() {
        ResponseEntity<Quote> responseEntity = restTemplate.exchange(
                "/quote?categories=love",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        Quote quote = responseEntity.getBody();
        assertThat(quote).isNotNull();

        assertThat(quote.getId()).isPositive();
        assertThat(quote.getQuoteText()).isNotEmpty();
        assertThat(quote.getAuthor().getName()).isNotEmpty();
    }

    @Test
    void test_not_found_a_random_quote() {
        ResponseEntity<ResponseError> responseEntity = restTemplate.exchange(
                "/quote?categories=lovetestetete",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isNotNull();
        ResponseError responseError = responseEntity.getBody();
        assertThat(responseError).isNotNull();

        assertThat(responseError.getType()).isEqualTo("Not found!");
        assertThat(responseError.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(responseError.getMessage()).isEqualTo("Quote not found!");
        assertThat(responseError.getDetails()).isNotNull();
        assertThat(responseError.getPath()).isEqualTo("uri=/quote");
    }

    @Test
    void test_skipped_request_param_random_quote() {
        ResponseEntity<ResponseError> responseEntity = restTemplate.exchange(
                "/quote",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        ResponseError responseError = responseEntity.getBody();
        assertThat(responseError).isNotNull();

        assertThat(responseError.getType()).isEqualTo("Validation");
        assertThat(responseError.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseError.getMessage()).isEqualTo("Required request parameter 'categories' for method parameter type List is not present");
        assertThat(responseError.getDetails()).isNotNull();
        assertThat(responseError.getPath()).isEqualTo("uri=/quote");
    }

    @Test
    void test_skipped_request_param_random_quote1() {

        when(this.quoteRepository.findRandomQuoteByCategoriesExcludingPreviousId(anyList()))
                .thenThrow(new QueryTimeoutException("Simulated database timeout"));

        ResponseEntity<ResponseError> responseEntity = restTemplate.exchange(
                "/quote?categories=non_existent_category",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        verify(this.quoteRepository).findRandomQuoteByCategoriesExcludingPreviousId(anyList());

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isNotNull();
        ResponseError responseError = responseEntity.getBody();
        assertThat(responseError).isNotNull();

        assertThat(responseError.getType()).isEqualTo("Server Error");
        assertThat(responseError.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(responseError.getMessage()).isEqualTo("Something went wrong");
        assertThat(responseError.getDetails()).isNotNull();
        assertThat(responseError.getPath()).isEqualTo("uri=/quote");
    }
}
