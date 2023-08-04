package com.example.demo.Exception;

public class MatrizCurricularException extends RuntimeException {

    public MatrizCurricularException(String message) {
        super(message);
    }

    public static class MatrizCurricularNotFoundException extends MatrizCurricularException {

        public MatrizCurricularNotFoundException(String message) {
            super(message);
        }
    }

    public static class MatrizCurricularInvalidDataException extends MatrizCurricularException {

        public MatrizCurricularInvalidDataException(String message) {
            super(message);
        }
    }

    public static class MatrizCurricularDeletionException extends MatrizCurricularException {

        public MatrizCurricularDeletionException(String message) {
            super(message);
        }
    }
}
