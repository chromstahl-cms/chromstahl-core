package software.kloud.kmscore.util;

public final class Constants {
    public static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    /**
     * Requirements:
     * <p>
     * - 8 - 400 Characters (Upper bound was chosen this high on purpose -> we don't want to not allow extra long passwords)<br>
     * - At least 1 digit<br>
     * - At least 1 special character (@#$%!)<br>
     * - At least 1 uppercase character<br>
     * - At least 1 lowercase character<br>
     */
    public static final String PASSWORD_REGEX = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,400})";
}
