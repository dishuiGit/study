package cn.com.ptpress.cdm.ds.es;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.http.HttpInfo;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.painless.PainlessPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 方便测试的ES节点
 *
 * @author jimo
 */
public class EmbeddedEsNode implements AutoCloseable {

    private final Node node;
    private volatile boolean started;

    private EmbeddedEsNode(Node node) {
        this.node = node;
        this.started = false;
    }

    @SuppressWarnings("UnstableApiUsage")
    public synchronized static EmbeddedEsNode create() {
        final File data = Files.createTempDir();
        data.deleteOnExit();
        final File home = Files.createTempDir();
        home.deleteOnExit();
        final Settings settings = Settings.builder()
                .put("node.name", "es-node")
                .put("network.host", "localhost")
                .put("http.type", "netty4")
                .put("path.home", home.getAbsolutePath())
                .put("path.data", data.getAbsolutePath())
                .build();
        final LocalNode node = new LocalNode(settings, Arrays.asList(Netty4Plugin.class, PainlessPlugin.class));
        return new EmbeddedEsNode(node);
    }

    public void start() {
        Preconditions.checkState(!started, "Already Started");
        try {
            node.start();
            this.started = true;
        } catch (NodeValidationException e) {
            e.printStackTrace();
        }
    }

    public TransportAddress httpAddress() {
        Preconditions.checkState(started, "node is not started");
        final NodesInfoResponse response =
                node.client().admin().cluster().prepareNodesInfo().execute().actionGet();
        Preconditions.checkState(response.getNodes().size() != 1, "expect single node");
        final NodeInfo node = response.getNodes().get(0);
        return node.getInfo(HttpInfo.class).address().boundAddresses()[0];
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void close() throws Exception {
        node.close();
        for (String name : Arrays.asList("path.home", "path.data")) {
            if (node.settings().get(name) != null) {
                final File file = new File(node.settings().get(name));
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    private static class LocalNode extends Node {

        public LocalNode(Settings settings, Collection<Class<? extends Plugin>> classpathPlugins) {
            super(InternalSettingsPreparer.prepareEnvironment(settings, Collections.emptyMap(), null,
                    () -> "node_name"), classpathPlugins, false);
        }
    }
}
