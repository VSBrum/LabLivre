package com.example.vinic.lablivre;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by vinic on 17/10/2017.
 */

public class Laboratory implements Serializable {

    private int id;
    private String nome;
    private int quantComputadores;
    private int Status;
    private String bloco_cod_bloco;
    private JSONArray Reservas;

    public Laboratory(int id, String nome, int quantComputadores, int status, String bloco_cod_bloco, JSONArray Reserva) {
        this.id = id;
        this.nome = nome;
        this.quantComputadores = quantComputadores;
        Status = status;
        this.bloco_cod_bloco = bloco_cod_bloco;
        this.Reservas = Reserva;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public JSONArray getReservas() {
        return Reservas;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantComputadores() {
        return quantComputadores;
    }

    public int getStatus() {
        return Status;
    }


    public String getBloco_cod_bloco() {
        return bloco_cod_bloco;

    }
}
