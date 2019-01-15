package com.example.zabuza.dovizhesaplaadamm;

public class JsonResponse {
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "response='" + response + '\'' +
                '}';
    }

    public String response;

}
