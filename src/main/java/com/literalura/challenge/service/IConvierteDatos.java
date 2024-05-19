package com.literalura.challenge.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
