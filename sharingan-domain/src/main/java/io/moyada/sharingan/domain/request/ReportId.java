package io.moyada.sharingan.domain.request;

import io.moyada.sharingan.domain.ValueObject;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ReportId implements ValueObject {

    private final String id;

    public ReportId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
