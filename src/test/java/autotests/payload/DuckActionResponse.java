package autotests.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuckActionResponse {
    @JsonProperty
    private String message;
    @JsonProperty
    private String sound;

    public String getMessage() { return message; }
    public String getSound() { return sound; }

    public DuckActionResponse setMessage(String message) {
        this.message = message;
        return this;
    }
    public DuckActionResponse setSound(String sound) {
        this.sound = sound;
        return this;
    }
}
