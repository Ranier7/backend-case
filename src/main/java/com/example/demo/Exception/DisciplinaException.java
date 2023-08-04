package com.example.demo.Exception;

public class DisciplinaException extends RuntimeException {

    public DisciplinaException(String message) {
        super(message);
    }

    public static class DisciplinaNotFoundException extends DisciplinaException {

        public DisciplinaNotFoundException(String message) {
            super(message);
        }
    }

    public static class DisciplinaInvalidDataException extends DisciplinaException {

        public DisciplinaInvalidDataException(String message) {
            super(message);
        }
    }

    public static class DisciplinaDeletionException extends DisciplinaException {

        public DisciplinaDeletionException(String message) {
            super(message);
        }
    }
}
