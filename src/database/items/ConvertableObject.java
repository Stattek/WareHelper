package database.items;

import java.util.List;
import java.util.Map;

public abstract class ConvertableObject {
    public abstract List<String> getAttributeKeys();

    public abstract List<String> getSubObjects();
}
