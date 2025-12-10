package autotests.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuckPropertiesResponse
{
    @JsonProperty
    private String color;

    @JsonProperty
    private double height;

    @JsonProperty
    private String material;

    @JsonProperty
    private String sound;

    @JsonProperty
    private String wingsState;

    public String getColor() {
        return color;
    }

    public double getHeight() {
        return height;
    }

    public String getMaterial() {
        return material;
    }

    public String getSound() {
        return sound;
    }

    public String getWingsState() {
        return wingsState;
    }

    public DuckPropertiesResponse setColor(String color) {
        this.color = color;
        return this;
    }

    public DuckPropertiesResponse setHeight(double height) {
        this.height = height;
        return this;
    }

    public DuckPropertiesResponse setMaterial(String material) {
        this.material = material;
        return this;
    }

    public DuckPropertiesResponse setSound(String sound) {
        this.sound = sound;
        return this;
    }

    public DuckPropertiesResponse setWingsState(String wingsState) {
        this.wingsState = wingsState;
        return this;
    }
}
