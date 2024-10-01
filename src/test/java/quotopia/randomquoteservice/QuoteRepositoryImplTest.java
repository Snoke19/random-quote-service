package quotopia.randomquoteservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import quotopia.randomquoteservice.repository.impl.QuoteRepositoryImpl;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class QuoteRepositoryImplTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private QuoteRepositoryImpl quoteRepository;

    @Test
    void test_null_parameter_fetch_a_random_quote() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> quoteRepository.findRandomQuoteByCategoriesExcludingPreviousId(null));

        assertThat(illegalArgumentException.getMessage()).isEqualTo("Categories cannot be null or empty");
    }

    @Test
    void test_empty_list_parameter_fetch_a_random_quote() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> quoteRepository.findRandomQuoteByCategoriesExcludingPreviousId(new ArrayList<>()));

        assertThat(illegalArgumentException.getMessage()).isEqualTo("Categories cannot be null or empty");
    }
}
