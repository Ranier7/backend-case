package com.example.demo.Exception;

public class SemestreException extends RuntimeException {

    public SemestreException(String message) {
        super(message);
    }

    public static class SemestreNotFoundException extends SemestreException {

        public SemestreNotFoundException(String message) {
            super(message);
        }
    }

    public static class SemestreInvalidDataException extends SemestreException {

        public SemestreInvalidDataException(String message) {
            super(message);
        }
    }

    public static class SemestreDeletionException extends SemestreException {

        public SemestreDeletionException(String message) {
            super(message);
        }
    }
}
