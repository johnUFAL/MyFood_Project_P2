package br.ufal.ic.myfood.controllers;

import br.ufal.ic.myfood.exceptions.Entregas.*;
import br.ufal.ic.myfood.exceptions.Usuarios.Entregador.*;
import br.ufal.ic.myfood.models.Entrega;
import br.ufal.ic.myfood.models.Pedido;

import java.util.Map;
import java.util.LinkedHashMap;

public class ControladorDeEntregas {
    private Map<Integer, Entrega> entrega;
    private int proximoId;

    public ControladorDeEntregas() {
        this.entrega = new LinkedHashMap<>();
        this.proximoId = 1;
    }

    public Map<Integer, Entrega> getEntrega() { return entrega; }
    public void setEntrega(Map<Integer, Entrega> entrega) { this.entrega = entrega; }

    public int getProximoId() { return proximoId; }
    public void setProximoId(int proximoId) { this.proximoId = proximoId; }

    public void zerar() {
        this.entrega.clear();
        this.proximoId = 1;
    }

    public int gerarId() {return this.proximoId++;}

    public int criarEntrega(Pedido pedido, int idEntregador, String destino) {
        int id = gerarId();

        Entrega novaEntrega = new Entrega(id, pedido.getCliente(), pedido.getEmpresa(), pedido.getNumero(), idEntregador, destino);
        novaEntrega.setProdutos(pedido.getProdutos());
        this.entrega.put(id, novaEntrega);
        return id;
    }

    public Entrega buscarEntregaPorId(int id) throws Exception {
        if (!this.entrega.containsKey(id)) {
            throw new NaoExisteEntregaID();
        }
        return this.entrega.get(id);
    }

    public int getEntrega(int idPedido) throws Exception {
        for (Entrega ent : this.entrega.values()) {
            if (ent.getPedido() == idPedido) {
                return ent.getId();
            }
        }
        throw  new NaoExisteEntregaID();
    }

    public int getIdEntrega(int idPedido) throws Exception {
        for (Entrega e : this.entrega.values()) {
            if (e.getPedido() == idPedido) {
                return e.getId();
            }
        }
        throw new NaoExisteEntregaID();
    }
}
