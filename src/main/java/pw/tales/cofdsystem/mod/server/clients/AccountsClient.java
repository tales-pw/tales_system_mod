package pw.tales.cofdsystem.mod.server.clients;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Singleton
public class AccountsClient {

  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();

  private final String url;
  private final String apiKey;

  @Inject
  public AccountsClient(String url, String apiKey) {
    this.url = url;
    this.apiKey = apiKey;
  }

  public CompletableFuture<Binding> get_binding_by_username(String username) {
    return this.request(
        "binding/get_by_username",
        Collections.singletonMap("username", username),
        Binding.class
    );
  }

  private <T> CompletableFuture<T> request(String path, Object data, Class<T> clazz) {
    String serializedData = this.gson.toJson(data);

    Request request = new Request.Builder()
        .post(RequestBody.create(MediaType.get("application/json"), serializedData))
        .url(String.format("%s/%s", this.url, path))
        .build();

    CompletableFuture<T> future = new CompletableFuture<>();

    this.client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
          if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
          }

          AccountsClient.this.gson.fromJson(responseBody.string(), clazz);
        }
      }
    });

    return future;
  }

  public static class Binding {

    public String dn;
    public String username;
  }
}
