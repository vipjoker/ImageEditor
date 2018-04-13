import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Value {
    private SimpleStringProperty name;
    private SimpleStringProperty value;


    public Value(String name, String  value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public SimpleStringProperty getName() {
        return name;
    }


    public SimpleStringProperty getValue() {
        return value;
    }




}
