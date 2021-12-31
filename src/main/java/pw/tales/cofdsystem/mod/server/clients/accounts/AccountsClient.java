package pw.tales.cofdsystem.mod.server.clients.accounts;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.ParametersAreNonnullByDefault;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pw.tales.cofdsystem.mod.server.clients.accounts.exceptions.NotFound;
import pw.tales.cofdsystem.mod.server.clients.accounts.exceptions.UnknownResponse;

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

  public CompletableFuture<Binding> getBindingByUsername(String username) {
    HashMap<String, Object> data = new HashMap<>();
    data.put("username", username);
    return this.request("binding/get_by_username", data, Binding.class);
  }

  private <T> CompletableFuture<T> request(String path, Map<String, Object> data, Class<T> clazz) {
    data.put("apiKey", this.apiKey);
    String serializedData = this.gson.toJson(data);

    Request request = new Request.Builder()
        .post(RequestBody.create(MediaType.get("application/json"), serializedData))
        .url(String.format("%s/%s", this.url, path))
        .build();

    CompletableFuture<T> future = new CompletableFuture<>();

    this.client.newCall(request).enqueue(new Callback() {
      @Override
      @ParametersAreNonnullByDefault
      public void onFailure(Call call, IOException e) {
        future.completeExceptionally(e);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void onResponse(Call call, Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
          String responseData = responseBody.string();

          if (response.code() == 404) {
            throw new NotFound();
          }

          if (!response.isSuccessful()) {
            throw new UnknownResponse(response.code(), responseData);
          }

          T result = AccountsClient.this.gson.fromJson(responseData, clazz);
          future.complete(result);
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
