package database.items;

import java.util.List;

public interface ConvertableObject {
    public List<String> getAttributeKeys();
    public List<String> getAllAttributes();
}
