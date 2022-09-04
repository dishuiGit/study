package csv.udx;

import java.util.ArrayList;
import java.util.List;

/**
 * Example of a user-defined aggregate function (UDAF).
 */
public class UDAF {
    public List<Object> init() {
        return new ArrayList<>();
    }

    public List add(List accumulator, Object v) {
        accumulator.add(v);
        return accumulator;
    }

    public List result(List accumulator) {
        return accumulator;
    }
}