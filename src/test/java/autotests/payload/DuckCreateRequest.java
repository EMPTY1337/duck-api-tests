package autotests.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DuckCreateRequest {
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

    public String getColor() { return color; }
    public double getHeight() { return height; }
    public String getMaterial() { return material; }
    public String getSound() { return sound; }
    public String getWingsState() { return wingsState; }

    public DuckCreateRequest setColor(String color) {
        this.color = color;
        return this;
    }
    public DuckCreateRequest setHeight(double height) {
        this.height = height;
        return this;
    }
    public DuckCreateRequest setMaterial(String material) {
        this.material = material;
        return this;
    }
    public DuckCreateRequest setSound(String sound) {
        this.sound = sound;
        return this;
    }
    public DuckCreateRequest setWingsState(String wingsState) {
        this.wingsState = wingsState;
        return this;
    }
}
