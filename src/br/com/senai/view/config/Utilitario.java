package br.com.senai.view.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utilitario {
	
	public Utilitario() {}

	private static final String BASE_URL = "https://api.sendgrid.com/v3/mail/send";
	private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request modifiedRequest = originalRequest.newBuilder()
                		.header("Authorization", "Bearer " + CarregarPropriedades.getPropertyOf("api.key"))
                        .header("Content-Type", "application/json")
                        .build();
                return chain.proceed(modifiedRequest);
            }) 
            .build();

    public void enviarEmail(String mensagem, String nomeRestaurante) throws IOException {
        String json = "{"
                + "\"personalizations\":[{"
                + "\"to\":[{\"email\":\""
                + CarregarPropriedades.getPropertyOf("to.email")
                + "\",\"name\":\"" 
                + CarregarPropriedades.getPropertyOf("to.name") + "\"}],"
                + "\"subject\":\"Email de atualização de horário do restaurante (" + nomeRestaurante + ")\""
                + "}],"
                + "\"content\":[{"
                + "\"type\":\"text/html\","
                + "\"value\":\"" + mensagem + "\""
                + "}],"
                + "\"from\":{"
                + "\"email\":\""
                + CarregarPropriedades.getPropertyOf("to.email")
                + "\",\"name\":\"" 
                + CarregarPropriedades.getPropertyOf("to.name") + "\""
                + "},"
                + "\"reply_to\":{"
                + "\"email\":\""
                + CarregarPropriedades.getPropertyOf("to.email") 
                + "\",\"name\":\"" 
                + CarregarPropriedades.getPropertyOf("to.name") + "\""
                + "}"
                + "}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        assertEquals(202, response.code());

        response.close();
    }
}
