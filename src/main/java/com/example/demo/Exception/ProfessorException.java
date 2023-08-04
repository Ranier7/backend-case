package com.example.demo.Exception;

public class ProfessorException extends RuntimeException {

    public ProfessorException(String message) {
        super(message);
    }

    public static class ProfessorNotFoundException extends ProfessorException {

        public ProfessorNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProfessorInvalidDataException extends ProfessorException {

        public ProfessorInvalidDataException(String message) {
            super(message);
        }
    }

    public static class ProfessorDeletionException extends ProfessorException {

        public ProfessorDeletionException(String message) {
            super(message);
        }
    }
}
