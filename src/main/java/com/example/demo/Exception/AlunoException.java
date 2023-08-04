package com.example.demo.Exception;

public class AlunoException extends RuntimeException {

    public AlunoException(String message) {
        super(message);
    }

    public static class AlunoNotFoundException extends AlunoException {

        public AlunoNotFoundException(String message) {
            super(message);
        }
    }

    public static class AlunoInvalidDataException extends AlunoException {

        public AlunoInvalidDataException(String message) {
            super(message);
        }
    }

    public static class AlunoDeletionException extends AlunoException {

        public AlunoDeletionException(String message) {
            super(message);
        }
    }
}
