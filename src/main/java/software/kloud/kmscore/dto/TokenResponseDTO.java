package software.kloud.kmscore.dto;

public class TokenResponseDTO extends AbstractResponseDTO {
    private String token;

    public TokenResponseDTO() {
    }

    public TokenResponseDTO(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
