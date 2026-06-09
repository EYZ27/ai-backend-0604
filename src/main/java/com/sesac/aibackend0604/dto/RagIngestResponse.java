package com.sesac.aibackend0604.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RagIngestResponse(
        String filename,
        int pages,
        @JsonProperty("chunks_added") int chunksAdded,
        @JsonProperty("total_chunks") int totalChunks
) {
}
