package com.example.polarion;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("Simple test running...");
        String token = System.getenv("POLARION_TOKEN");
        System.out.println("Token length: " + (token != null ? token.length() : "null"));
        System.out.println("First 20 chars: " + (token != null ? token.substring(0, Math.min(20, token.length())) : "null"));
    }
}
