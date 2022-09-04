package cn.com.ptpress.cdm.ds.pg;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

/**
 * https://github.com/yandex-qatools/postgresql-embedded
 * http://www.h2database.com/html/main.html
 * https://github.com/opentable/otj-pg-embedded
 *
 * @author jimo
 */
public class EmbeddedPGDb {

    private static String url;
    private static String user = "postgres";
    private static String password = "postgres";
    private static EmbeddedPostgres postgres;

    @BeforeAll
    static void beforeAll() throws IOException {
        postgres = new EmbeddedPostgres(V9_6);
        /*
        https://github.com/yandex-qatools/postgresql-embedded
        第一次跑时，使用不带缓存的配置，会自动将postgresql-9.6.11-1-windows-x64-binaries下载到本地home目录
        然后拷贝并解压到任意目录，再使用缓存配置跑，避免每次解压
         */
        // url = postgres.start("localhost", 5432, "postgres", user, password, ImmutableList.of());
        url = postgres.start(cachedRuntimeConfig(Paths.get("E:\\tmp\\postgresql-9.6.11-1-windows-x64-binaries\\pgsql")),
                "localhost", 5432, "postgres", user, password, ImmutableList.of());
        System.out.println("启动集成PG，url=" + url);
        // 从文件导入数据
        try {
            final File file = new File("src/main/resources/pg/data.sql");
            postgres.getProcess().get().importFromFile(file);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    void testPG() throws SQLException {
        String sql = "select \"code\" from PG.films where \"code\" ='movie' limit 2";
        try (Connection conn = DriverManager.getConnection("jdbc:calcite:model=src/main/resources/model.json")) {
//            final Statement statement = conn.createStatement();
//            assertTrue(statement.execute("SELECT * FROM films limit 2"));
//            assertTrue(statement.getResultSet().next());
//            assertEquals("movie", statement.getResultSet().getString("code"));

            final Statement statement = conn.createStatement();
            final ResultSet rs = statement.executeQuery(sql);
            final int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    System.out.println(rs.getObject(i + 1));
                }
            }
        }
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}
