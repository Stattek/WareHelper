package database.items;

import java.util.List;

public abstract class ConvertableObject {

    public abstract List<String> getAttributeKeys();

    public abstract List<String> getAllAttributes();

    public abstract List<DataType> getAttributeDataTypes();
}
