package bioskopkuy.model;

public abstract class AbstractEntity {
    protected String id; // Atau int id, tergantung kebutuhan
    protected String name;

    public AbstractEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract String getDisplayInfo();
}