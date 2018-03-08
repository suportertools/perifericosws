/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.perifericosws.entity;
 
public class RetornoJson {

    private Integer nr_pessoa;
    private String nome;
    private String foto;
    private String observacao;
    private Integer nr_codigo_erro;
    private String mensagem;
    private Integer via;
    private Boolean liberado;

    public RetornoJson() {
        this.nr_pessoa = null;
        this.nome = "";
        this.foto = "";
        this.observacao = "";
        this.nr_codigo_erro = null;
        this.mensagem = "";
        this.via = null;
        this.liberado = false;
    }

    public RetornoJson(Integer nr_pessoa, String nome, String foto, String observacao, Integer nr_codigo_erro, String mensagem, Integer via, Boolean liberado) {
        this.nr_pessoa = nr_pessoa;
        this.nome = nome;
        this.foto = foto;
        this.observacao = observacao;
        this.nr_codigo_erro = nr_codigo_erro;
        this.mensagem = mensagem;
        this.via = via;
        this.liberado = liberado;
    }

    public Integer getNr_pessoa() {
        return nr_pessoa;
    }

    public void setNr_pessoa(Integer nr_pessoa) {
        this.nr_pessoa = nr_pessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getNr_codigo_erro() {
        return nr_codigo_erro;
    }

    public void setNr_codigo_erro(Integer nr_codigo_erro) {
        this.nr_codigo_erro = nr_codigo_erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getVia() {
        return via;
    }

    public void setVia(Integer via) {
        this.via = via;
    }

    public Boolean getLiberado() {
        return liberado;
    }

    public void setLiberado(Boolean liberado) {
        this.liberado = liberado;
    }
}
