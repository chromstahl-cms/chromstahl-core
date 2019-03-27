package software.kloud.kmscore.dto;

public class RegisterResponseDTO extends AbstractResponseDTO {
    private String token;

    public RegisterResponseDTO() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
