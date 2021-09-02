package network.arkane.provider.blockcypher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoundRobinTest {

    private Iterator<String> values;

    @BeforeEach
    void setUp() {
        values = new RoundRobin<>(Arrays.asList("a", "b", "c")).iterator();
    }


    @Test
    void hasValues() {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            results.add(values.next());
        }
        assertThat(Collections.frequency(results, "a")).isEqualTo(100);
        assertThat(Collections.frequency(results, "b")).isEqualTo(100);
        assertThat(Collections.frequency(results, "c")).isEqualTo(100);
    }
}
