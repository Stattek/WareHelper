package database.items;

import java.util.Map;

public abstract class ObjectFactory {
    protected ConvertableObject convertableObject;

    public abstract Item createItem(Map<String, String> data);

    public abstract Bundle createBundle();

    public abstract Category createCategory();

}
