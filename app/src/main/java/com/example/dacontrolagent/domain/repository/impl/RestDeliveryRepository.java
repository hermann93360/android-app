package com.example.dacontrolagent.domain.repository.impl;

import android.util.Base64;
import android.util.Log;

import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.domain.repository.DeliveryRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestDeliveryRepository implements DeliveryRepository {

    private OkHttpClient client = new OkHttpClient();

    public RestDeliveryRepository() {
    }

    @Override
    public List<Delivery> findAllDeliveryByDeliverEmailAndCurrentDate(String email, LocalDate currentDate) {
        List<Delivery> deliveries = new ArrayList<>();
        Request request = new Request.Builder()
                .url("http://192.168.1.128:8080/deliveries")
                .addHeader("email", email)
                .build();

        try  {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();

                Log.i("e", responseBody);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                        .create();

                Type userListType = new TypeToken<List<Delivery>>(){}.getType();
                return gson.fromJson(responseBody, userListType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    @Override
    public void updatePhotoOfDelivery(String packageNumber, byte[] photoOfDeliver) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), photoOfDeliver);

        Request request = new Request.Builder()
                .url("http://192.168.1.128:8080/photos")
                .post(requestBody)
                .addHeader("packageNumber", packageNumber)
                .build();

        try  {
            Response response = client.newCall(request).execute();
            Log.e("e", String.valueOf(response.code()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDeliverStatus(String packageNumber) {
        Request request = new Request.Builder()
                .url("http://192.168.1.128:8080/delivered")
                .addHeader("packageNumber", packageNumber)
                .build();

        try  {
            Response response = client.newCall(request).execute();
            Log.e("e", String.valueOf(response.code()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ByteArrayToBase64TypeAdapter implements JsonDeserializer<byte[]> {
        @Override
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Base64.decode(json.getAsString(), Base64.DEFAULT);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Could not decode bytes", e);
            }
        }
    }
}
