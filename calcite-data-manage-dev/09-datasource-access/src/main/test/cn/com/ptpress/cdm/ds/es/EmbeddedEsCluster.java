package cn.com.ptpress.cdm.ds.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.transport.TransportAddress;

import java.util.Objects;

/**
 * ES使用入口
 *
 * @author jimo
 */
public class EmbeddedEsCluster {

    private final EmbeddedEsNode node;
    private RestClient client;

    private EmbeddedEsCluster(EmbeddedEsNode node) {
        this.node = Objects.requireNonNull(node, "node");
        this.node.start();
        restClient();
    }

    static class Singleton {
        static final EmbeddedEsCluster INSTANCE = new EmbeddedEsCluster(EmbeddedEsNode.create());
    }

    public static EmbeddedEsCluster create() {
        return Singleton.INSTANCE;
    }

    /**
     * 初始化并返回客户端
     */
    RestClient restClient() {
        if (client != null) {
            return client;
        }
        final TransportAddress address = node.httpAddress();
        this.client = RestClient.builder(
                new HttpHost(address.getAddress(), address.getPort())
        ).build();
        return this.client;
    }
}
