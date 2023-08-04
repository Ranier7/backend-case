package com.example.demo.Exception;

public class CursoException extends RuntimeException {

    public CursoException(String message) {
        super(message);
    }

    public static class CursoNotFoundException extends CursoException {

        public CursoNotFoundException(String message) {
            super(message);
        }
    }

    public static class CursoInvalidDataException extends CursoException {

        public CursoInvalidDataException(String message) {
            super(message);
        }
    }

    public static class CursoDeletionException extends CursoException {

        public CursoDeletionException(String message) {
            super(message);
        }
    }
}
