package cn.com.ptpress.cdm;

import org.apache.calcite.avatica.jdbc.JdbcMeta;
import org.apache.calcite.avatica.remote.Driver;
import org.apache.calcite.avatica.remote.LocalService;
import org.apache.calcite.avatica.server.HttpServer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 服务启动入口
 *
 * @author jimo
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8765;

//        final HttpServer server = org.apache.calcite.avatica.server.Main.start(
//                new String[]{JdbcMeta.class.getName()}, port, AvaticaJsonHandler::new);
        String url = "jdbc:mysql://localhost:3306/db_cdm";
        final JdbcMeta meta = new JdbcMeta(url, "root", "root");
        final LocalService service = new LocalService(meta);

        final HttpServer server = new HttpServer.Builder<>()
                .withPort(port)
//                .withHandler(service, Driver.Serialization.JSON)
                .withHandler(service, Driver.Serialization.PROTOBUF)
//                .withBasicAuthentication(readAuthProperties(), new String[]{"users"})
                .withDigestAuthentication(readAuthProperties(), new String[]{"users"})
//                .withSpnego()
                .build();

        server.start();
        server.join();
    }

    private static String readAuthProperties() throws UnsupportedEncodingException {
        return URLDecoder.decode(Main.class.getResource("/auth-users.properties").getFile(), "UTF-8");
    }
}
