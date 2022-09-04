package cn.com.ptpress.cdm.ds.es;

import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EsTest {
    final EmbeddedEsCluster es = EmbeddedEsCluster.create();

    @Test
    void testEs() {
        final RestClient restClient = es.restClient();
        assertNotNull(restClient);
    }
}