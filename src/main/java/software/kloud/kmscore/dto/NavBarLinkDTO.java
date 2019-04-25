package software.kloud.kmscore.dto;

import software.kloud.KMSPluginSDK.NavBarEntity;

public class NavBarLinkDTO {
    private String linkText;
    private String path;

    public NavBarLinkDTO(String linkText, String path) {
        this.linkText = linkText;
        this.path = path;
    }

    public NavBarLinkDTO(NavBarEntity entity) {
        this.linkText = entity.getLinkText();
        this.path = entity.getPath();
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
