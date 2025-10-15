/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.exceptions;

/**
 * Custom exception for input or data validation errors.
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
