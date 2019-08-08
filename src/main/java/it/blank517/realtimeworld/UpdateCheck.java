package it.blank517.realtimeworld;

import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiConsumer;

class UpdateCheck {

    private static final String SPIGOT_URL = "https://api.spigotmc.org/legacy/update.php?resource=%d";
    private final JavaPlugin javaPlugin;
    private String currentVersion;
    private int resourceId = -1;
    private BiConsumer<VersionResponse, String> versionResponse;

    private UpdateCheck(@Nonnull JavaPlugin javaPlugin) {
        this.javaPlugin = Objects.requireNonNull(javaPlugin, "javaPlugin");
        this.currentVersion = javaPlugin.getDescription().getVersion();
    }

    static UpdateCheck of(@Nonnull JavaPlugin javaPlugin) {
        return new UpdateCheck(javaPlugin);
    }

    @SuppressWarnings("unused")
    UpdateCheck currentVersion(@Nonnull String currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    @SuppressWarnings("SameParameterValue")
    UpdateCheck resourceId(int resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    UpdateCheck handleResponse(@Nonnull BiConsumer<VersionResponse, String> versionResponse) {
        this.versionResponse = versionResponse;
        return this;
    }

    void check() {
        Objects.requireNonNull(this.javaPlugin, "javaPlugin");
        Objects.requireNonNull(this.currentVersion, "currentVersion");
        Preconditions.checkState(this.resourceId != -1, "resource id not set");
        Objects.requireNonNull(this.versionResponse, "versionResponse");

        Bukkit.getScheduler().runTaskAsynchronously(this.javaPlugin, () -> {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(String.format(SPIGOT_URL, this.resourceId)).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty(HttpHeaders.USER_AGENT, "Mozilla/5.0");

                String fetchedVersion = new Scanner(httpURLConnection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

                boolean latestVersion = fetchedVersion.equalsIgnoreCase(this.currentVersion);

                Bukkit.getScheduler().runTask(this.javaPlugin, () -> this.versionResponse.accept(latestVersion ? VersionResponse.LATEST : VersionResponse.FOUND_NEW, latestVersion ? this.currentVersion : fetchedVersion));
            } catch (IOException exception) {
                exception.printStackTrace();
                Bukkit.getScheduler().runTask(this.javaPlugin, () -> this.versionResponse.accept(VersionResponse.UNAVAILABLE, null));
            }
        });
    }

    public enum VersionResponse {
        LATEST,
        FOUND_NEW,
        UNAVAILABLE
    }
}
