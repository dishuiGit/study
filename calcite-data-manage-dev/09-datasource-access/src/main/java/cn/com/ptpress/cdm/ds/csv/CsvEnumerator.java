package cn.com.ptpress.cdm.ds.csv;

import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.util.Source;

import java.io.BufferedReader;
import java.io.IOException;

public class CsvEnumerator<E> implements Enumerator<E> {

    private E current;

    private BufferedReader br;

    CsvEnumerator(Source source) {
        try {
            this.br = new BufferedReader(source.reader());
            this.br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public E current() {
        return current;
    }

    /**
     * 判断是否有下一行，并更新current
     * @return
     */
    @Override
    public boolean moveNext() {
        try {
            String line = br.readLine();
            if (line == null) {
                return false;
            }
            current = (E) line.split(",");    // 如果是多列，这里要多个值
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
