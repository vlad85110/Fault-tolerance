package kirilov.me.crack.entity;

import java.util.List;
import java.util.UUID;

public record WorkerToManagerRequest(UUID requestId, int partNumber, List<String> words) {}
