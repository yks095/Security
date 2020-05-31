package com.kiseok.studylogin.config.jwt;

public class JwtConstants {
    public static final long SERIAL_VERSION_UID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String SECRET = "kiseok";
    public static final String PRE_FIX = "Bearer ";
    public static final String HEADER = "Authorization";
}
