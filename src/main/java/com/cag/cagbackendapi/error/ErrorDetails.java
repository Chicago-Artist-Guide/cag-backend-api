package com.cag.cagbackendapi.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ErrorDetails {
    private final Date time;
    private final String restErrorMessage;
    private final String detailedMessage;

    public Date getTime() {
        return time;
    }

    public String getRestErrorMessage() {
        return restErrorMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    @JsonCreator
    public ErrorDetails(@JsonProperty("time") Date time,
                        @JsonProperty("restErrorMessage") String restErrorMessage,
                        @JsonProperty("detailedMessage") String detailedMessage) {
        this.time = time;
        this.restErrorMessage = restErrorMessage;
        this.detailedMessage = detailedMessage;
    }
}
