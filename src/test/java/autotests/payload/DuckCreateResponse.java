package autotests.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DuckCreateResponse {
    @JsonProperty
    private int id;

    public int getId() {
        return id;
    }

    public DuckCreateResponse setId(int id) {
        this.id = id;
        return this;
    }
}
