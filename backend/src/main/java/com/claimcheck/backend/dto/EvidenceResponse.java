package com.claimcheck.backend.dto;

public class EvidenceResponse {

    private Long id;
    private Long claimId;
    private String content;
    private String url;
    private String title;
    private String sourceName;

    public EvidenceResponse(
            Long id,
            Long claimId,
            String content,
            String url,
            String title,
            String sourceName) {

        this.id = id;
        this.claimId = claimId;
        this.content = content;
        this.url = url;
        this.title = title;
        this.sourceName = sourceName;
    }

    public Long getId() {
        return id;
    }

    public Long getClaimId() {
        return claimId;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceName() {
        return sourceName;
    }
}