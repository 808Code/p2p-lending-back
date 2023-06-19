package com.bonsai.accountservice.dto.request;

import java.util.UUID;

public record AdminKycMessageRequest(String adminKycMessage, String email) {
}
