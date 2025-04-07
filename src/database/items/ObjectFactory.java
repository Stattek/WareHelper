package database.items;

import java.util.Map;

public abstract class ObjectFactory {
    protected ConvertableObject convertableObject;

    public abstract Bundle createBundle();

    public abstract Category createCategory();

    public abstract DateInfo createDateInfo();

    public abstract EconomyInfo createEconoomyInfo();

    public abstract Item createItem(Map<String, String> data);

    public abstract Preference createPreferenceInfo();

}
