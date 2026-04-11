import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Facade {

    private ControladorDeUsuarios controladorUsuarios;

    public Facade() {
        carregarDados();
    }

    public void zerarSistema() {
        this.controladorUsuarios.zerar();
    }

    public void encerrarSistema() {
        salvarDados();
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) throws Exception {
        this.controladorUsuarios.criarCliente(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        this.controladorUsuarios.criarDono(nome, email, senha, endereco, cpf);
    }

    public int login(String email, String senha) throws Exception {
        return this.controladorUsuarios.login(email, senha);
    }

    public String getAtributoUsuario(int id, String atributo) throws Exception {
        return this.controladorUsuarios.getAtributoUsuario(id, atributo);
    }

    private void salvarDados() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("banco_myfood.xml")))) {
            encoder.writeObject(this.controladorUsuarios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarDados() {
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("banco_myfood.xml")))) {
            this.controladorUsuarios = (ControladorDeUsuarios) decoder.readObject();
        } catch (Exception e) {
            this.controladorUsuarios = new ControladorDeUsuarios();
        }
    }
}