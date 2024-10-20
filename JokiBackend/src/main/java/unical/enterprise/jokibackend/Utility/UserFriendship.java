package unical.enterprise.jokibackend.Utility;

public enum UserFriendship {
    NOT_FRIENDS,
    FRIENDS,
    PENDING, // l'utente loggato ha mandato la richiesta all'altro utente
    REQUESTED // l'altro utente ha mandato la richiesta all'utente loggato
}
