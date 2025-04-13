package database.items;

import java.util.List;

public abstract class ConvertableObject {
    public enum DataType {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        DATE
    }

    public abstract List<String> getAttributeKeys();

    public abstract List<String> getSubObjects();

    public abstract List<String> getAllAttributes();

    public abstract List<DataType> getAttributeDataTypes();
}
