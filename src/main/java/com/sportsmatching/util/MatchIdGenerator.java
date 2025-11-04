package com.sportsmatching.util;

import java.security.SecureRandom;

public class MatchIdGenerator {
    // Caracteres sin I, O, 0, 1 para evitar confusión visual
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Genera un ID legible y corto para un partido.
     * Formato: MAT-XXXXXX donde XXXXXX es un código alfanumérico de 6 caracteres.
     * Ejemplo: MAT-A3B9X2
     */
    public static String generate() {
        StringBuilder code = new StringBuilder("MAT-");
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return code.toString();
    }
}

