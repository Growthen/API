package dev.growthen.api.common.constants;

/**
 * Mensajes de error estándar para las respuestas de la API.
 * <p>
 * Contiene los mensajes descriptivos asociados a las excepciones y fallos
 * de autenticación, autorización y gestión de usuarios.
 */
public final class ErrorMessages {

    private ErrorMessages() {
        // Constructo privado para prevenir instanciación
    }

    // --- Autenticación y Autorización ---

    /** Mensaje cuando el usuario autenticado no posee los permisos requeridos. */
    public static final String FORBIDDEN_ACCESS =
            "Access denied: You don't have permission to perform this action";

    /** Mensaje cuando la solicitud requiere autenticación y el token no es válido o está ausente. */
    public static final String UNAUTHORIZED_ACCESS =
            "Unauthorized access: You need to be logged in to perform this action";

    /** Mensaje para credenciales de inicio de sesión incorrectas. */
    public static final String INVALID_CREDENTIALS =
            "Invalid credentials";


    // --- Gestión de Usuarios ---

    /** Mensaje cuando el usuario solicitado no existe en la base de datos. */
    public static final String USER_NOT_FOUND =
            "User not found";

    /** Mensaje cuando se intenta registrar un usuario con un identificador ya existente. */
    public static final String USER_ALREADY_EXISTS =
            "User already exists";

    /** Mensaje cuando se intenta registrar un usuario con un nombre de usuario ya existente. */
    public static final String USERNAME_ALREADY_EXISTS =
            "Username already exists";

    public static final String EMAIL_ALREADY_EXISTS =
            "Email already exists";

    public static final String INVALID_REFRESH_TOKEN =
            "Invalid or expired refresh token";

    public static final String REFRESH_TOKEN_EXPIRED =
            "Refresh token has expired. Please sign in again";

    public static final String REFRESH_TOKEN_REVOKED =
            "Refresh token has been revoked";


}
