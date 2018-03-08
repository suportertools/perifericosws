/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.perifericosws.entity;

public class Catraca {

    private Integer id;

    private Integer numero;
    private Integer porta;
    private Integer quantidade_digitos;
    private Boolean bloquear_sem_foto;

    private Integer tipo_giro_catraca;
    private String lado_giro_catraca;
    private Integer departamento;
    private String servidor_foto;

    private Boolean servidor_beep;
    private Boolean biometrico;
    private Boolean leitor_biometrico_externo;
    private Boolean grava_frequencia_catraca;
    private Boolean verificacao_de_biometria;
    private Boolean verificacao_de_liberacao;

    private String cliente;
    private String IP;
    private String mac;
    private Integer servidor;

    private Boolean atualizar;

    public Catraca() {
        this.id = -1;
        this.numero = 1;
        this.porta = 3570;
        this.quantidade_digitos = 0;
        this.bloquear_sem_foto = false;
        this.tipo_giro_catraca = 0; // 0 - SAIDA, 1 - ENTRADA, 2 - AMBAS
        this.departamento = 0; // DEPARTAMENTO -- 12 (CLUBE)  11 (ACADEMIA)
        this.servidor_foto = "";
        this.servidor_beep = false;
        this.biometrico = false;
        this.leitor_biometrico_externo = false;
        this.grava_frequencia_catraca = false;
        this.verificacao_de_biometria = false;
        this.verificacao_de_liberacao = false;
        this.cliente = "";
        this.IP = "";
        this.mac = ""; // MAC DO SERVIDOR
        this.servidor = null;
        this.atualizar = true;
    }

    public Catraca(Integer id, Integer numero, Integer porta, Integer quantidade_digitos, Boolean bloquear_sem_foto, Integer tipo_giro_catraca, String lado_giro_catraca, Integer departamento, String servidor_foto, Boolean servidor_beep, Boolean biometrico, Boolean leitor_biometrico_externo, Boolean grava_frequencia_catraca, Boolean verificacao_de_biometria, Boolean verificacao_de_liberacao, String cliente, String IP, String mac, Integer servidor, Boolean atualizar) {
        this.id = id;
        this.numero = numero;
        this.porta = porta;
        this.quantidade_digitos = quantidade_digitos;
        this.bloquear_sem_foto = bloquear_sem_foto;
        this.tipo_giro_catraca = tipo_giro_catraca;
        this.lado_giro_catraca = lado_giro_catraca;
        this.departamento = departamento;
        this.servidor_foto = servidor_foto;
        this.servidor_beep = servidor_beep;
        this.biometrico = biometrico;
        this.leitor_biometrico_externo = leitor_biometrico_externo;
        this.grava_frequencia_catraca = grava_frequencia_catraca;
        this.verificacao_de_biometria = verificacao_de_biometria;
        this.verificacao_de_liberacao = verificacao_de_liberacao;
        this.cliente = cliente;
        this.IP = IP;
        this.mac = mac;
        this.servidor = servidor;
        this.atualizar = atualizar;
    }

//    public Catraca get_catraca(Integer numero) {
//        for (Catraca c : lista_catraca) {
//            if (c.getNumero().equals(numero)) {
//                return c;
//            }
//        }
//        return null;
//    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getPorta() {
        return porta;
    }

    public void setPorta(Integer porta) {
        this.porta = porta;
    }

    public Integer getQuantidade_digitos() {
        return quantidade_digitos;
    }

    public void setQuantidade_digitos(Integer quantidade_digitos) {
        this.quantidade_digitos = quantidade_digitos;
    }

    public Boolean getBloquear_sem_foto() {
        return bloquear_sem_foto;
    }

    public void setBloquear_sem_foto(Boolean bloquear_sem_foto) {
        this.bloquear_sem_foto = bloquear_sem_foto;
    }

    public Integer getTipo_giro_catraca() {
        return tipo_giro_catraca;
    }

    public void setTipo_giro_catraca(Integer tipo_giro_catraca) {
        this.tipo_giro_catraca = tipo_giro_catraca;
    }

    public String getLado_giro_catraca() {
        return lado_giro_catraca;
    }

    public void setLado_giro_catraca(String lado_giro_catraca) {
        this.lado_giro_catraca = lado_giro_catraca;
    }

    public Boolean getAtualizar() {
        return atualizar;
    }

    public void setAtualizar(Boolean atualizar) {
        this.atualizar = atualizar;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public String getServidor_foto() {
        return servidor_foto;
    }

    public void setServidor_foto(String servidor_foto) {
        this.servidor_foto = servidor_foto;
    }

    public Boolean getServidor_beep() {
        return servidor_beep;
    }

    public void setServidor_beep(Boolean servidor_beep) {
        this.servidor_beep = servidor_beep;
    }

    public Boolean getBiometrico() {
        return biometrico;
    }

    public void setBiometrico(Boolean biometrico) {
        this.biometrico = biometrico;
    }

    public Boolean getLeitor_biometrico_externo() {
        return leitor_biometrico_externo;
    }

    public void setLeitor_biometrico_externo(Boolean leitor_biometrico_externo) {
        this.leitor_biometrico_externo = leitor_biometrico_externo;
    }

    public Boolean getGrava_frequencia_catraca() {
        return grava_frequencia_catraca;
    }

    public void setGrava_frequencia_catraca(Boolean grava_frequencia_catraca) {
        this.grava_frequencia_catraca = grava_frequencia_catraca;
    }

    public Boolean getVerificacao_de_biometria() {
        return verificacao_de_biometria;
    }

    public void setVerificacao_de_biometria(Boolean verificacao_de_biometria) {
        this.verificacao_de_biometria = verificacao_de_biometria;
    }

    public Boolean getVerificacao_de_liberacao() {
        return verificacao_de_liberacao;
    }

    public void setVerificacao_de_liberacao(Boolean verificacao_de_liberacao) {
        this.verificacao_de_liberacao = verificacao_de_liberacao;
    }

//    public List<Catraca> getLista_catraca() {
//        return lista_catraca;
//    }
//
//    public void setLista_catraca(List<Catraca> lista_catraca) {
//        this.lista_catraca = lista_catraca;
//    }
    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the servidor
     */
    public Integer getServidor() {
        return servidor;
    }

    /**
     * @param servidor the servidor to set
     */
    public void setServidor(Integer servidor) {
        this.servidor = servidor;
    }

}
